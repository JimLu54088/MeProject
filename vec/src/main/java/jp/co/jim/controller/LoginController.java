package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.common.Constants;
import jp.co.jim.common.JwtTokenUtil;
import jp.co.jim.entity.ErrorDTO;
import jp.co.jim.entity.SearchCriteriaEntity;
import jp.co.jim.service.LoginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);
    private static final String LOG_HEADER = "[" + LoginController.class.getSimpleName() + "] :: ";

    private static final String ERROR_LOG_HEADER = "[" + LoginController.class.getName() + "] :: ";

    @Autowired
    private LoginService service;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private Environment environment;

    @Value("${maximum_save_search_criteria}")
    private int maximum_save_search_criteria;


    @PostMapping("/Login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {

        String username = loginData.get("username");
        String password = loginData.get("password");

        //Output received reuqestBody
        Gson gson = new Gson();
        String jsonLoginData = gson.toJson(loginData);
        logger.info(LOG_HEADER + "received requestBody : " + jsonLoginData);


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
        logger.debug(LOG_HEADER + "received requestBody : " + jsonCriteriaData);

        SearchCriteriaEntity searchEntity = new SearchCriteriaEntity();

        searchEntity.setKur(criteriaData.get("kur"));
        searchEntity.setProject_jya_code(criteriaData.get("project_jya_code"));
        searchEntity.setModel_code(criteriaData.get("model_code"));
        searchEntity.setColor(criteriaData.get("color"));
        searchEntity.setManufacter_date(criteriaData.get("manufacter_date"));
        searchEntity.setS_c_id(criteriaData.get("criteriaName"));
        searchEntity.setUser_id(criteriaData.get("userId"));


        try {
            List<Map<String, Object>> resultList =  service.searchSingleVEC(searchEntity);

            String jsonResultList = gson.toJson(resultList);
            logger.debug(LOG_HEADER + "jsonResultList : " + jsonResultList);



            return ResponseEntity.ok(Map.of("token", "kkk"));

        } catch (Exception ex) {

            ex.printStackTrace();


            logger.error(ERROR_LOG_HEADER + "Error while insert Search CriteriaData into DB : " + ex);

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
        logger.debug(LOG_HEADER + "received requestBody : " + strCriteriaData);

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


        logger.debug(LOG_HEADER + "received userId : " + userId);

        try {
            List<SearchCriteriaEntity> criteriaList = service.selectCriteriaDataByID(userId);

            Gson gson = new Gson();
            String strcriteriaList = gson.toJson(criteriaList);
            logger.debug(LOG_HEADER + "strcriteriaList : " + strcriteriaList);


            return ResponseEntity.ok(criteriaList);

        } catch (Exception ex) {
            logger.error(ERROR_LOG_HEADER + "Error while insert Search CriteriaData into DB : " + ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while insert Search CriteriaData into DB!!");
        }


    }

    @GetMapping("/deleteSavedCriteria")
    public ResponseEntity<?> deleteSavedCriteria(@RequestParam String user_id, String s_c_id) {


        logger.debug(LOG_HEADER + "received userId : " + user_id + " received s_c_id : " + s_c_id);

        try {
            service.deleteSavedCriteriaByIDAndName(user_id, s_c_id);

            return ResponseEntity.ok("{\"status\": \"ok\"}");

        } catch (Exception ex) {
            logger.error(ERROR_LOG_HEADER + "Error while insert delete CriteriaData into DB : " + ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while delete Search CriteriaData into DB!!");
        }


    }


}
