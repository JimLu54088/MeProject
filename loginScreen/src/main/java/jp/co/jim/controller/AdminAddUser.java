package jp.co.jim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
public class AdminAddUser {

    @PostMapping("/AdminAddNewUser")
    public ResponseEntity<String> adminLogin(@RequestBody Map<String, String> addNewUserData) {

        String username = addNewUserData.get("username");
        String password = addNewUserData.get("password");
        String repassword = addNewUserData.get("reEnteredpassword");

        //check if password and re-entered pass is same or not
        if(!password.equals(repassword)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First password and second password is not the same. Please re-try");
        }

//        if (loginService.checkAdminLogin(username, password) == 1) {
//            return ResponseEntity.ok("Login successful!");
//        } else {
//            logger.info(LOG_HEADER + "received username : " + username + " received password : " + password);
//            System.out.println("received username : " + username + " received password : " + password);
//
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed!");
//        }
    }

}
