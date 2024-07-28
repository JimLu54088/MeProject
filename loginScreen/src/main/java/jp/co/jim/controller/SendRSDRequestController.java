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



        String responseJSONBoey = "";

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
