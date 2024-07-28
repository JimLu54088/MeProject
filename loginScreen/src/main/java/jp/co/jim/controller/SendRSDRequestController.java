package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.bean.utils.AppUtils;
import jp.co.jim.entity.UserEntity;
import jp.co.jim.repository.UserActionMapper;
import jp.co.jim.service.DGUserService;
import jp.co.jim.service.RSDRequestHandler;
import jp.co.jim.service.UserActionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SendRSDRequestController {

    @Autowired
    private DGUserService dGUserService;

    @Autowired
    private UserActionService userActionService;

    @Autowired
    private UserActionMapper userActionMapper;

    @Autowired
    private RSDRequestHandler rsdRequestHandler;

    private static final Logger logger = LogManager.getLogger(SendRSDRequestController.class);
    private static final String LOG_HEADER = "[" + SendRSDRequestController.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + SendRSDRequestController.class.getName() + "] :: ";

    @PostMapping("/sendRSDRequestLink")
    public ResponseEntity<String> changeFDIUserPassword(@RequestBody String requestJSONData) {

//        String username = changeFDIUserPasswordData.get("username");
//        String password = changeFDIUserPasswordData.get("password");
//        String newpassword = changeFDIUserPasswordData.get("newpassword");
//        String reEntereNewdpassword = changeFDIUserPasswordData.get("reEntereNewdpassword");
//
//
//        //Output received reuqestBody
//        Gson gson = new Gson();
//        String jsonLoginData = gson.toJson(changeFDIUserPasswordData);
        logger.info(LOG_HEADER + "received requestBody : " + requestJSONData);

        boolean isRequestBodyAJSONFormat = AppUtils.checkRequstBodyJsonValidation(requestJSONData);

//        //check if the inputed user_id existed in DB or not.
//        UserEntity user = userActionMapper.findByUsername(username);
//
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not in DB");
//        }
//
//        //check if oldPassowrd is correct or not
//        if (!password.equals(user.getUser_password())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is not correct. Please re-try");
//        }
//
//
//        //check if password and re-entered pass is same or not
//        if (!newpassword.equals(reEntereNewdpassword)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First password and second password is not the same. Please re-try");
//        }
//
//        //check if old password is same as new one
//        if (newpassword.equals(user.getUser_password())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input password is same as previous one. Please re-try");
//        }
//
//
////        //check if insert user is already in db or not
////        int countOfChecking = service.checkinsertingExisting(username);
////
////        if (service.checkinsertingExisting(username) >= 1) {
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user is already registered. Please use others");
////        }
//        user.setNew_password(newpassword);
//
//        dGUserService.updateDGUserPassword(user);
//
//        // Record successful login action
//        userActionService.saveUserAction(username, "User changed password successfully.");

        String responseJSONBoey = "{\"REQUESTRESPONSE\": {\"REQUEST\": {\"HEADER\": {\"REQ_TO\": \"JIMSYSTEM\",\"REQ_ID\": \"JIM_TEST\",\"REQ_SYS_SPECIFIC\": {\"CAM_SR_ID\": \"JIM_TEST\",\"REQUEST_TYPE\": \"JIM3REST\"},\"ERRORS\": {},\"REQ_FROM\": \"JIM5P\"},\"CONTENTS\": {\"KURUMA\": \"TTTMM0001\",\"API_TYPE\": \"getCConfiGurationData\",\"IDTRANS\": \"JIM5P20145894\",\"REQ_JSON\": \"{\\\"transactionID\\\":\\\"JIM5P20145894\\\",\\\"FINFamily\\\":\\\"TTT\\\",\\\"client_req_time\\\":\\\"\\\",\\\"CEUDATA\\\":[{\\\"CEUProptType\\\":\\\"UUUSD\\\",\\\"CEU_ADDRESS\\\":\\\"2324D\\\",\\\"CEU_configurationLink\\\":\\\"478544TY7B\\\"}]}\"}},\"RESPONSE\": {\"CONTENTS\": {\"FILED1\":\"4564564AAA\",\"FIELD2\":\"M000\",\"FIELD3\":\"The JIM3REST request process is successfully.\"},\"HEADER\": {\"ERRORS\": {\"ERROR\":[{\"RETURN_CODE\":\"00\",\"REASON_CODE\":\"DDDFFF000\"}]}}}}}";

        if (isRequestBodyAJSONFormat) {

            try {
                //Add current time in JSON
                requestJSONData = AppUtils.updateClient_req_timeInJSON(requestJSONData);


                responseJSONBoey = rsdRequestHandler.rsdRequestHandler(requestJSONData);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            return ResponseEntity.ok(responseJSONBoey);
        } else {
            logger.info(LOG_HEADER + "received requestBody is not JSON format");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input Request Body is not JSON format.");
        }


    }
}
