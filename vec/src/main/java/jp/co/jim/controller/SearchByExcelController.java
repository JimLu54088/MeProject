package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.common.Constants;
import jp.co.jim.common.JwtTokenUtil;
import jp.co.jim.entity.ErrorDTO;
import jp.co.jim.entity.SearchCriteriaEntity;
import jp.co.jim.entity.SearchResultEntity;
import jp.co.jim.entity.WarningDTO;
import jp.co.jim.service.LoginService;
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

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SearchByExcelController {

    private static final Logger logger = LogManager.getLogger(SearchByExcelController.class);
    private static final String LOG_HEADER = "[" + SearchByExcelController.class.getSimpleName() + "] :: ";

    private static final String WARN_LOG_HEADER = "[" + SearchByExcelController.class.getSimpleName() + "] :: ";

    private static final String ERROR_LOG_HEADER = "[" + SearchByExcelController.class.getName() + "] :: ";

    @Autowired
    private SearchResultService searchResultService;


    @Autowired
    private LoginService loginService;

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
                List<SearchCriteriaEntity> searchCriteriaList = readSearchCriteriaFromExcel(String.valueOf(targetExcelFile));


                List<Map<String, Object>> resultList = new ArrayList<>();

                for (SearchCriteriaEntity entity : searchCriteriaList) {
                    // 將 searchMapper.search(entity) 的結果查詢出來
                    List<Map<String, Object>> result = loginService.searchSingleVEC(entity);

                    // 將每行結果添加到 resultList 中
                    for (Map<String, Object> row : result) {
                        // 創建一個新的 Map 來存儲當前行的數據
                        Map<String, Object> rowData = new HashMap<>();

                        // 將需要的欄位從原始結果中取出來，並放到新的 Map 中
                        rowData.put("KUR", row.get("KUR"));
                        rowData.put("PROJ_F_CODE", row.get("PROJ_F_CODE"));
                        rowData.put("MODEL_CD", row.get("MODEL_CD"));
                        rowData.put("COLOR", row.get("COLOR"));
                        rowData.put("MANUF_DATE", row.get("MANUF_DATE"));

                        // 動態處理不在固定欄位中的其他欄位
                        for (String key : row.keySet()) {
                            if (!isFixedColumn(key)) {
                                rowData.put(key, row.get(key));
                            }
                        }

                        // 將這個新建的 Map 添加到 resultList 中
                        resultList.add(rowData);
                    }
                }


                if (resultList.isEmpty()) {


                    logger.warn(WARN_LOG_HEADER + Constants.noRecordFound);


                    SearchResultEntity searchResultEntity = new SearchResultEntity();

                    searchResultEntity.setS_r_id(userIdAndSearchTitle.get("searchTitle"));
                    searchResultEntity.setUser_id(userIdAndSearchTitle.get("userId"));
                    searchResultEntity.setStatus("1");
                    searchResultEntity.setErr_msg(Constants.noRecordFound);


                    //Insert search result into FB
                    try {
                        logger.info(LOG_HEADER + "Insert search result into DB.");
                        searchResultService.saveSearchResultIntoDB(searchResultEntity);
                    } catch (Exception e) {

                        logger.error(ERROR_LOG_HEADER + "Error while insert search result into DB : ", e);

                        ErrorDTO errorResponse = new ErrorDTO(Constants.WSE001, e.toString());

                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

                    }


                    WarningDTO warningResponse = new WarningDTO(Constants.WSW001, Constants.noRecordFound_toFrontEnd);


                    return ResponseEntity.ok(warningResponse);

                }


                String tempOutPutCsvPath = resultZipFileLocation + userIdAndSearchTitle.get("userId") + "\\" + System.currentTimeMillis() + "_temp.csv";
                String timestamp = Constants.dateTimeFormatyyyyMMdd__HHmmss.format(new Date());
                String outPutCsvPath = resultZipFileLocation + userIdAndSearchTitle.get("userId") + "\\" + timestamp + ".csv";

                Path pathOutPutCsvPath = Paths.get(outPutCsvPath);
                // 提取文件名
                String strFileNameOnly = pathOutPutCsvPath.getFileName().toString();


                // 返回下載鏈接
                String fileDownloadUrl = "/api/download?fileName=" + strFileNameOnly + "&userId=" + userIdAndSearchTitle.get("userId");

                //Insert search result into FB
                SearchResultEntity searchResultEntity = new SearchResultEntity();

                searchResultEntity.setS_r_id(userIdAndSearchTitle.get("searchTitle"));
                searchResultEntity.setUser_id(userIdAndSearchTitle.get("userId"));
                searchResultEntity.setStatus("0");
                searchResultEntity.setDwn_lnk(fileDownloadUrl);
                searchResultEntity.setErr_msg("");


                try {
                    logger.info(WARN_LOG_HEADER + "Insert search result into DB.");
                    searchResultService.saveSearchResultIntoDB(searchResultEntity);
                } catch (Exception e) {

                    logger.error(ERROR_LOG_HEADER + "Error while insert search result into DB : ", e);

                    ErrorDTO errorResponse = new ErrorDTO(Constants.WSE001, e.toString());

                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

                }


                try {

                    generateCSV(resultList, tempOutPutCsvPath);

                    removeDuplicatedLine(tempOutPutCsvPath, outPutCsvPath);


                    return ResponseEntity.ok(Collections.singletonMap("downloadUrl", fileDownloadUrl));
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


    public List<SearchCriteriaEntity> readSearchCriteriaFromExcel(String filePath) throws IOException {
        List<SearchCriteriaEntity> searchCriteriaList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming the data is on the first sheet

            for (int rowIndex = 1; rowIndex <= maxSearchByExcelRows; rowIndex++) { // Skip header row
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    if ("".equals(getCellValue(row.getCell(1))) || getCellValue(row.getCell(1)) == null) {
                        continue;
                    }
                    SearchCriteriaEntity entity = new SearchCriteriaEntity();
                    entity.setKur(getCellValue(row.getCell(1)));
                    entity.setProject_jya_code(getCellValue(row.getCell(2)));
                    entity.setModel_code(getCellValue(row.getCell(3)));
                    entity.setColor(getCellValue(row.getCell(4)));
                    entity.setManufacter_date(getCellValue(row.getCell(5)));
                    searchCriteriaList.add(entity);
                }
            }
        }

        return searchCriteriaList;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        }
        return cell.getStringCellValue();
    }


    public void generateCSV(List<Map<String, Object>> resultList, String outputFilePath) throws IOException {
        // 用於收集所有動態列
        Set<String> dynamicColumns = new LinkedHashSet<>();

        // 1. 遍歷所有行，找到所有的動態列（排除固定列）
        for (Map<String, Object> row : resultList) {
            for (String key : row.keySet()) {
                if (!isFixedColumn(key)) {
                    dynamicColumns.add(key);
                }
            }
        }

        // 2. 寫入 CSV 表頭 (固定部分在最左邊)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // 固定欄位部分
            String searchSingleKURCSVHeader = "KUR,PROJ_F_CODE,MODEL_CD,COLOR,MANUF_DATE";
            writer.write(searchSingleKURCSVHeader);

            // 動態欄位部分
            for (String column : dynamicColumns) {
                writer.write("," + column);
            }
            writer.newLine();

            // 3. 寫入資料
            for (Map<String, Object> row : resultList) {
                // 固定欄位部分
                writer.write(
                        row.getOrDefault("KUR", "") + "," +
                                row.getOrDefault("PROJ_F_CODE", "") + "," +
                                row.getOrDefault("MODEL_CD", "") + "," +
                                row.getOrDefault("COLOR", "") + "," +
                                row.getOrDefault("MANUF_DATE", "")
                );

                // 動態欄位部分
                for (String column : dynamicColumns) {
                    writer.write("," + row.getOrDefault(column, "")); // 如果没有值，則默認為空
                }
                writer.newLine();
            }

            //Remove duplicated result


        }
    }

    // 判斷是否是固定列
    private boolean isFixedColumn(String column) {
        return column.equals("KUR") ||
                column.equals("PROJ_F_CODE") ||
                column.equals("MODEL_CD") ||
                column.equals("COLOR") ||
                column.equals("MANUF_DATE");
    }

    public void removeDuplicatedLine(String inputFilePath, String outputFilePath) throws IOException {


        // 用來追蹤已經寫入的行
        Set<String> linesSet = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // 如果該行還未處理過，則將其寫入輸出文件
                if (linesSet.add(line)) {
                    writer.write(line);
                    writer.newLine(); // 寫入新行
                }
            }
        } catch (IOException e) {
            logger.error(ERROR_LOG_HEADER + "Error while remove duplicated line in csv : ", e);
            throw e;

        } finally {
            File fielInputFile = new File(inputFilePath);
            fielInputFile.delete();


        }
    }


}
