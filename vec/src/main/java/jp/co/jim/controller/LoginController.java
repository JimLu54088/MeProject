package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.common.Constants;
import jp.co.jim.common.JwtTokenUtil;
import jp.co.jim.entity.ErrorDTO;
import jp.co.jim.entity.SearchCriteriaEntity;
import jp.co.jim.entity.WarningDTO;
import jp.co.jim.service.LoginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api")
public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);
    private static final String LOG_HEADER = "[" + LoginController.class.getSimpleName() + "] :: ";

    private static final String WARN_LOG_HEADER = "[" + LoginController.class.getSimpleName() + "] :: ";

    private static final String ERROR_LOG_HEADER = "[" + LoginController.class.getName() + "] :: ";

    @Autowired
    private LoginService service;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private Environment environment;

    @Value("${maximum_save_search_criteria}")
    private int maximum_save_search_criteria;

    @Value("${resultZipFileLocation}")
    private String resultZipFileLocation;


    @PostMapping("/Login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {

        String username = loginData.get("username");
        String password = loginData.get("password");

        //Output received reuqestBody
        Gson gson = new Gson();
        String jsonLoginData = gson.toJson(loginData);
        logger.info(LOG_HEADER + "received requestBody for Login: " + jsonLoginData);


        // Validate the credentials

        if (service.checkAdminLogin(username, password) == 1) {
            // Record successful login action
//            userActionService.saveUserAction(username, "Admin Login successful");


            // 验证通过，生成JWT
            try {
                String token = jwtTokenUtil.generateToken(username);

                // 返回JWT给客户端
                return ResponseEntity.ok(Map.of("token", token));
            } catch (Exception e) {
                logger.error(ERROR_LOG_HEADER + "Token getting failed." + e);
                throw new RuntimeException("Token getting failed.");

            }

        } else {
            // Record unsuccessful login action
//            userActionService.saveUserAction(username, "Login failed!");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin Login failed!");
        }
    }

    @PostMapping("/searchSingleVec")
    public ResponseEntity<?> searchSingleVec(@RequestBody Map<String, String> criteriaData) {


        //Output received reuqestBody
        Gson gson = new Gson();
        String jsonCriteriaData = gson.toJson(criteriaData);
        logger.debug(LOG_HEADER + "received requestBody for searchSingleVec: " + jsonCriteriaData);


        if (this.areAllValuesEmpty(criteriaData)) {

            logger.warn(WARN_LOG_HEADER + "No Criteria Specified.");

            WarningDTO warningResponse = new WarningDTO("WSW002", "No Criteria Specified.");


            return ResponseEntity.ok(warningResponse);

        }


        SearchCriteriaEntity searchEntity = new SearchCriteriaEntity();

        searchEntity.setKur(criteriaData.get("kur"));
        searchEntity.setProject_jya_code(criteriaData.get("project_jya_code"));
        searchEntity.setModel_code(criteriaData.get("model_code"));
        searchEntity.setColor(criteriaData.get("color"));
        searchEntity.setManufacter_date(criteriaData.get("manufacter_date"));
        searchEntity.setS_c_id(criteriaData.get("criteriaName"));
        searchEntity.setUser_id(criteriaData.get("userId"));


        try {
            List<Map<String, Object>> resultList = service.searchSingleVEC(searchEntity);

            if (resultList.isEmpty()) {


                logger.warn(WARN_LOG_HEADER + "No record found.");

                WarningDTO warningResponse = new WarningDTO("WSW001", "No record found. Please change another criteria.");


                return ResponseEntity.ok(warningResponse);

            }

            String jsonResultList = gson.toJson(resultList);
            logger.debug(LOG_HEADER + "jsonResultList : " + jsonResultList);

            //Create personal folder
            String resultZipFileLocationWithUser = resultZipFileLocation + criteriaData.get("userId") + "\\";
            // 動態生成當前時間的檔案名
            Files.createDirectories(Paths.get(resultZipFileLocationWithUser));
            String timestamp = Constants.dateTimeFormatyyyyMMdd__HHmmss.format(new Date());
            String csvFileName = resultZipFileLocationWithUser + timestamp + ".csv";
            String zipFileName = resultZipFileLocationWithUser + timestamp + ".zip";

            // 生成 CSV 文件
            Path csvFile = Paths.get(csvFileName);
            try (BufferedWriter writer = Files.newBufferedWriter(csvFile)) {
                // 用於收集所有動態列
                Set<String> dynamicColumns = new LinkedHashSet<>();

                // 遍歷所有行，找到所有的動態列
                for (Map<String, Object> row : resultList) {
                    for (String key : row.keySet()) {
                        if (!isFixedColumn(key)) {
                            dynamicColumns.add(key);
                        }
                    }
                }

                // 寫入 CSV 表頭 (固定部分在最左邊)
                writer.write("KUR,PROJ_F_CODE,MODEL_CD,COLOR,MANUF_DATE");

                // 寫入動態欄位部分
                for (String column : dynamicColumns) {
                    writer.write("," + column);
                }
                writer.newLine();

                // 寫入資料
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
                        writer.write("," + row.getOrDefault(column, ""));
                    }
                    writer.newLine();
                }
            }


            // 壓縮 CSV 文件為 ZIP
            Path pathZipFile = Paths.get(zipFileName);
            try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(pathZipFile))) {
                ZipEntry zipEntry = new ZipEntry(csvFile.getFileName().toString());
                zipOut.putNextEntry(zipEntry);
                Files.copy(csvFile, zipOut);
                zipOut.closeEntry();
            }

            // 刪除原始 CSV 文件
            Files.delete(csvFile);

            // 提取文件名
            String strFileNameOnly = pathZipFile.getFileName().toString();


            // 返回下載鏈接
            String fileDownloadUrl = "/api/download?fileName=" + strFileNameOnly + "&userId=" + criteriaData.get("userId");
            return ResponseEntity.ok(Collections.singletonMap("downloadUrl", fileDownloadUrl));

        } catch (IOException ioe) {
            logger.error(ERROR_LOG_HEADER + "Error while generate result zip file : " + ioe);

            ErrorDTO errorResponse = new ErrorDTO("WSE004", ioe.getMessage());


            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception ex) {

            ex.printStackTrace();


            logger.error(ERROR_LOG_HEADER + "Error while search single vec data from DB : " + ex);

            ErrorDTO errorResponse = new ErrorDTO("WSE001", ex.getMessage());


            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }


    }

    @PostMapping("/saveSearchCriteria")
    public ResponseEntity<?> saveSearchCriteria(@RequestBody Map<String, String> criteriaData) {


        //check if records exceeds the limit
        String user_id = criteriaData.get("userId");

        service.countOfSavedSearchCriteriaByID(user_id);

        if (service.countOfSavedSearchCriteriaByID(user_id) == maximum_save_search_criteria) {

            ErrorDTO errorResponse = new ErrorDTO("WSE002", "Touch the limit of criteria saving. Cannot insert record anymore.");


            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        }


        SearchCriteriaEntity insertEntity = new SearchCriteriaEntity();

        insertEntity.setKur(criteriaData.get("kur"));
        insertEntity.setProject_jya_code(criteriaData.get("project_jya_code"));
        insertEntity.setModel_code(criteriaData.get("model_code"));
        insertEntity.setColor(criteriaData.get("color"));
        insertEntity.setManufacter_date(criteriaData.get("manufacter_date"));
        insertEntity.setS_c_id(criteriaData.get("criteriaName"));
        insertEntity.setUser_id(criteriaData.get("userId"));


        //Output received reuqestBody
        Gson gson = new Gson();
        String strCriteriaData = gson.toJson(criteriaData);
        logger.debug(LOG_HEADER + "received requestBody for saveSearchCriteria: " + strCriteriaData);

        try {
            service.insertSearchCriteriaData(insertEntity);
            return ResponseEntity.ok(Map.of("token", "kkk"));

        } catch (Exception ex) {
            logger.error(ERROR_LOG_HEADER + "Error while insert Search CriteriaData into DB : " + ex);

            ErrorDTO errorResponse = new ErrorDTO("WSE001", ex.getMessage());


            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }


    }

    @GetMapping("/getCriteriaList")
    public ResponseEntity<?> getCriteriaList(@RequestParam String userId) {


        logger.debug(LOG_HEADER + "[getCriteriaList] received userId : " + userId);

        try {
            List<SearchCriteriaEntity> criteriaList = service.selectCriteriaDataByID(userId);

            Gson gson = new Gson();
            String strcriteriaList = gson.toJson(criteriaList);
            logger.debug(LOG_HEADER + "strcriteriaList : " + strcriteriaList);


            return ResponseEntity.ok(criteriaList);

        } catch (Exception ex) {
            logger.error(ERROR_LOG_HEADER + "Error while getting saved criteria from DB : " + ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while getting saved criteria from DB!!");
        }


    }

    @GetMapping("/deleteSavedCriteria")
    public ResponseEntity<?> deleteSavedCriteria(@RequestParam String user_id, String s_c_id) {


        logger.debug(LOG_HEADER + "[deleteSavedCriteria] received userId : " + user_id + " received s_c_id : " + s_c_id);

        try {
            service.deleteSavedCriteriaByIDAndName(user_id, s_c_id);

            return ResponseEntity.ok("{\"status\": \"ok\"}");

        } catch (Exception ex) {
            logger.error(ERROR_LOG_HEADER + "Error while delete CriteriaData from DB : " + ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while delete Search CriteriaData from DB!!");
        }


    }

    //Download zip file
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName, String userId) {
        // 拼接文件路径
        Path filePath = Paths.get(resultZipFileLocation,userId, fileName);

        logger.debug(LOG_HEADER + "download target file location : " + filePath.toString());



        // 确保文件存在
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        // 创建 Resource 对象
        Resource resource = new FileSystemResource(filePath);

        // 返回响应
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }





    // 判斷是否為固定欄位
    private boolean isFixedColumn(String column) {
        return "KUR".equals(column) || "MODEL_CD".equals(column) || "PROJ_F_CODE".equals(column) ||
                "COLOR".equals(column) || "MANUF_DATE".equals(column);
    }

    //verify requestBody is empty for each item or not
    public boolean areAllValuesEmpty(Map<String, String> criteriaData) {
        return criteriaData.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("userId")) // 排除 userId 的檢查
                .allMatch(entry -> entry.getValue() == null || entry.getValue().trim().isEmpty());
    }


}
