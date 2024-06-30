package jp.co.jim.controller;

import jp.co.jim.common.Constants;
import jp.co.jim.response.DGLoginResponse;
import jp.co.jim.service.DGUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DGLoginController {

    @Autowired
    private DGUserService userService;

    @PostMapping("/DGlogin")
    public ResponseEntity<?> dglogin(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        String result = userService.handleLogin(username, password);

        DGLoginResponse response;

        if (result.equals(Constants.DGRP001)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin Login failed!");

        } else if (result.equals("First time login, please update your password!")) {
            response = new DGLoginResponse(Constants.DGRP002, "LoginSuccess");
            return ResponseEntity.ok(response);

        } else if (result.equals("Login successful! Please update your password within 7 days.")) {
            response = new DGLoginResponse(Constants.DGRP003, "LoginFailed");
            return ResponseEntity.ok(response);
        } else {
            response = new DGLoginResponse(Constants.DGRP000, "Login Successful");
            return ResponseEntity.ok(response);
        }
    }

//    @PostMapping("/updatePassword")
//    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> updateData) {
//        String username = updateData.get("username");
//        String newPassword = updateData.get("newPassword");
//
//        userService.updatePassword(username, newPassword);
//
//        return ResponseEntity.ok("Password updated successfully!");
//    }
}
