package jp.co.jim.controller;

import jp.co.jim.common.Constants;
import jp.co.jim.common.JwtTokenUtil;
import jp.co.jim.response.DGLoginResponse;
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
public class DGLoginController {

    @Autowired
    private DGUserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserActionService userActionService;

    private static final Logger logger = LogManager.getLogger(DGLoginController.class);
    private static final String LOG_HEADER = "[" + DGLoginController.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + DGLoginController.class.getName() + "] :: ";


    @PostMapping("/DGlogin")
    public ResponseEntity<?> dglogin(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        String result = userService.handleLogin(username, password);

        DGLoginResponse response;

        // 验证通过，生成JWT
        try {
            String token = jwtTokenUtil.generateToken(username);


            if (result.equals(Constants.DGRP001)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin Login failed!");

            } else if (result.equals("First time login, please update your password!")) {
                response = new DGLoginResponse(Constants.DGRP002, "LoginSuccess");

                // Record successful login action
                userActionService.saveUserAction(username, "User Login successful");
                return ResponseEntity.ok(response);

            } else if (result.equals("Login successful! Please update your password within 7 days.")) {
                response = new DGLoginResponse(Constants.DGRP003, "LoginFailed", token);
                // Record successful login action
                userActionService.saveUserAction(username, "User Login successful");
                return ResponseEntity.ok(response);
            } else {
                // Record successful login action
                userActionService.saveUserAction(username, "User Login successful");

                response = new DGLoginResponse(Constants.DGRP000, "Login Successful", token);
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            logger.error(ERROR_LOG_HEADER + "Token getting failed." + e);
            throw new RuntimeException("Token getting failed.");

        }
    }


}
