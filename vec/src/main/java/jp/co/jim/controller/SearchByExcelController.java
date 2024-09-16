package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.common.JwtTokenUtil;
import jp.co.jim.entity.SearchResultEntity;
import jp.co.jim.service.SearchResultService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

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





    @PostMapping("/searchByExcel")
    public ResponseEntity<?> searchByExcel(@RequestParam String userId,
                                           @RequestParam String searchTitle,
                                           @RequestPart("file") MultipartFile file) {


        // 在這裡處理接收到的檔案和參數
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is missing");
        }


        logger.debug(LOG_HEADER + "[getSearchResultList] received userId : " + userId);

        try {
            List<SearchResultEntity> searchResultList = service.selectSearchResultByID(userId);

            Gson gson = new Gson();
            String strcriteriaList = gson.toJson(searchResultList);
            logger.debug(LOG_HEADER + "strsearchResultList : " + strcriteriaList);


            return ResponseEntity.ok("File uploaded and processed successfully");

        } catch (Exception ex) {
            logger.error(ERROR_LOG_HEADER + "Error while getting search result from DB : ", ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while getting search result from DB!!");
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

        // 定義檔案儲存的路徑，檔名使用原始檔名
        String filePath = folderPath + "/" + file.getOriginalFilename();
        File destinationFile = new File(filePath);

        try {
            // 將上傳的檔案複製到目標位置
            file.transferTo(destinationFile);
            return ResponseEntity.ok("{\"status\": \"ok\"}");
        } catch (IOException e) {
            // 處理檔案上傳失敗的情況
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }


}
