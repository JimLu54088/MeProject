package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.common.Constants;
import jp.co.jim.common.JwtTokenUtil;
import jp.co.jim.entity.ErrorDTO;
import jp.co.jim.entity.SearchResultEntity;
import jp.co.jim.service.SearchResultService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SearchByExcelController {

    private static final Logger logger = LogManager.getLogger(SearchByExcelController.class);
    private static final String LOG_HEADER = "[" + SearchByExcelController.class.getSimpleName() + "] :: ";

    private static final String WARN_LOG_HEADER = "[" + SearchByExcelController.class.getSimpleName() + "] :: ";

    private static final String ERROR_LOG_HEADER = "[" + SearchByExcelController.class.getName() + "] :: ";

    @Autowired
    private SearchResultService service;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private Environment environment;

    @Value("${maximum_save_search_criteria}")
    private int maximum_save_search_criteria;

    @Value("${resultZipFileLocation}")
    private String resultZipFileLocation;

    @Value("${searchByExcelWorkFolderLocation}")
    private String searchByExcelWorkFolderLocation;

    @Value("${maxSearchByExcelRows}")
    private int maxSearchByExcelRows;


    @PostMapping("/searchByExcel")
    public ResponseEntity<?> searchByExcel(@RequestBody Map<String, String> userIdAndSearchTitle) {


        //Output received reuqestBody
        Gson gson = new Gson();
        String jsonUserIdAndSearchTitle = gson.toJson(userIdAndSearchTitle);
        logger.info(LOG_HEADER + "received requestBody for searchByExcel: " + jsonUserIdAndSearchTitle);

        //Check only one excel file exists in work folder

        String folderPath = searchByExcelWorkFolderLocation + userIdAndSearchTitle.get("userId"); // 資料夾路徑

        File folder = new File(folderPath);

        // 確認該路徑是一個資料夾
        if (!folder.isDirectory()) {
            logger.error(ERROR_LOG_HEADER + "Error while creating directory.");

            ErrorDTO errorResponse = new ErrorDTO(Constants.WSE007, "Error while creating directory.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        // 使用 FilenameFilter 過濾 Excel 檔案
        FilenameFilter excelFilter = (dir, name) -> name.endsWith(".xls") || name.endsWith(".xlsx");

        // 取得符合條件的 Excel 檔案
        File[] excelFiles = folder.listFiles(excelFilter);

        // 確認是否只有一個 Excel 檔案
        if (excelFiles == null) {

            logger.error(ERROR_LOG_HEADER + "Failed to read the folder or folder is empty.");

            ErrorDTO errorResponse = new ErrorDTO(Constants.WSE007, "Failed to read the folder or folder is empty.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

            //Search process only run when excel file exists only one.
        } else if (excelFiles.length == 1) {

            //Check Excel Condition
            File targetExcelFile = excelFiles[0]; // Excel 文件的路徑

            try (FileInputStream fis = new FileInputStream(targetExcelFile);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                // 假設數據在第一個工作表
                Sheet sheet = workbook.getSheetAt(0);

                int filledCellCount = 0;

                // 從 B2 (索引 1) 到 B201 (索引 200)
                for (int rowIndex = 1; rowIndex <= maxSearchByExcelRows; rowIndex++) {
                    Row row = sheet.getRow(rowIndex); // 獲取對應的行
                    if (row != null) {
                        Cell cell = row.getCell(1); // 獲取 B 列 (索引 1)
                        if (cell != null && cell.getCellType() != CellType.BLANK) {
                            filledCellCount++; // 如果儲存格不為空，計數 +1
                        }
                    }
                }

                logger.info(LOG_HEADER + "Filled cells in B2 to B" + (int) (maxSearchByExcelRows + 1) + " :: " + filledCellCount);


                try {


                    return ResponseEntity.ok("{\"status\": \"ok\"}");
                } catch (Exception ex) {
                    logger.error(ERROR_LOG_HEADER + "Error while getting search result from DB : ", ex);

                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while getting search result from DB!!");
                }


            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        } else if (excelFiles.length == 0) {
            logger.error(ERROR_LOG_HEADER + "No Excel files found in the folder.");

            ErrorDTO errorResponse = new ErrorDTO(Constants.WSE007, "No Excel files found in the folder.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);


        } else {
            logger.error(ERROR_LOG_HEADER + "There are multiple Excel files in the folder.");

            ErrorDTO errorResponse = new ErrorDTO(Constants.WSE007, "There are multiple Excel files in the folder.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }


    }


    @PostMapping("/uploadAndCheckExcel")
    public ResponseEntity<?> uploadAndCheckExcel(@RequestParam String userId,
                                                 @RequestParam String searchTitle,
                                                 @RequestPart("file") MultipartFile file) {


        // 定義儲存檔案的目標資料夾路徑
        String folderPath = searchByExcelWorkFolderLocation + userId;
        File folder = new File(folderPath);

        // 如果資料夾不存在，則創建該資料夾
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            org.apache.commons.io.FileUtils.cleanDirectory(folder);
        } catch (IOException ioe) {
            logger.error(ERROR_LOG_HEADER + "Error while clean work folder : ", ioe);

            ErrorDTO errorResponse = new ErrorDTO(Constants.WSE001, ioe.toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        }


        // 定義檔案儲存的路徑，檔名使用原始檔名
        String filePath = folderPath + "/" + file.getOriginalFilename();
        File destinationFile = new File(filePath);

        try {
            // 將上傳的檔案複製到目標位置
            file.transferTo(destinationFile);
            return ResponseEntity.ok("{\"status\": \"ok\"}");
        } catch (IOException e) {
            // 處理檔案上傳失敗的情況
            logger.error(ERROR_LOG_HEADER + "Error while uploading file : ", e);

            ErrorDTO errorResponse = new ErrorDTO(Constants.WSE001, e.toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}
