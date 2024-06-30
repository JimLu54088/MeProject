package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.entity.UserEntity;
import jp.co.jim.repository.UserActionMapper;
import jp.co.jim.service.AdminAddUserService;
import jp.co.jim.service.DGUserService;
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
public class DGChangePasswordController {
    @Autowired
    private DGUserService dGUserService;

    @Autowired
    private UserActionService userActionService;

    @Autowired
    private UserActionMapper userActionMapper;

    private static final Logger logger = LogManager.getLogger(DGChangePasswordController.class);
    private static final String LOG_HEADER = "[" + DGChangePasswordController.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + DGChangePasswordController.class.getName() + "] :: ";

    @PostMapping("/changeFDIUserPassword")
    public ResponseEntity<String> changeFDIUserPassword(@RequestBody Map<String, String> changeFDIUserPasswordData) {

        String username = changeFDIUserPasswordData.get("username");
        String password = changeFDIUserPasswordData.get("password");
        String newpassword = changeFDIUserPasswordData.get("newpassword");
        String reEntereNewdpassword = changeFDIUserPasswordData.get("reEntereNewdpassword");


        //Output received reuqestBody
        Gson gson = new Gson();
        String jsonLoginData = gson.toJson(changeFDIUserPasswordData);
        logger.info(LOG_HEADER + "received requestBody : " + jsonLoginData);

        //check if the inputed user_id existed in DB or not.
        UserEntity user = userActionMapper.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not in DB");
        }

        //check if oldPassowrd is correct or not
        if (!password.equals(user.getUser_password())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is not correct. Please re-try");
        }


        //check if password and re-entered pass is same or not
        if (!newpassword.equals(reEntereNewdpassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First password and second password is not the same. Please re-try");
        }

        //check if old password is same as new one
        if (newpassword.equals(user.getUser_password())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input password is same as previous one. Please re-try");
        }


//        //check if insert user is already in db or not
//        int countOfChecking = service.checkinsertingExisting(username);
//
//        if (service.checkinsertingExisting(username) >= 1) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user is already registered. Please use others");
//        }
        user.setNew_password(newpassword);

        dGUserService.updateDGUserPassword(user);

        // Record successful login action
        userActionService.saveUserAction(username, "User changed password successfully.");
        return ResponseEntity.ok("DGPasswordChange successfully.");
    }
}
