package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.service.AdminAddUserService;
import jp.co.jim.service.AdminLoginService;
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
public class AdminAddUser {

    @Autowired
    private AdminAddUserService service;

    private static final Logger logger = LogManager.getLogger(AdminAddUser.class);
    private static final String LOG_HEADER = "[" + AdminAddUser.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + AdminAddUser.class.getName() + "] :: ";

    @PostMapping("/AdminAddNewUser")
    public ResponseEntity<String> adminLogin(@RequestBody Map<String, String> addNewUserData) {

        String username = addNewUserData.get("username");
        String password = addNewUserData.get("password");
        String repassword = addNewUserData.get("reEnteredpassword");

        //Output received reuqestBody
        Gson gson = new Gson();
        String jsonLoginData = gson.toJson(addNewUserData);
        logger.info(LOG_HEADER + "received requestBody : " + jsonLoginData);

        //check if password and re-entered pass is same or not
        if (!password.equals(repassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First password and second password is not the same. Please re-try");
        }

        //check if insert user is already in db or not
        int countOfChecking = service.checkinsertingExisting(username);

        if (service.checkinsertingExisting(username) >= 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user is already registered. Please use others");
        }

        service.insertUser(username, password);
        return ResponseEntity.ok("Insert successful!");
    }

}
