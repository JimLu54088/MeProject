package jp.co.jim.controller;

import jp.co.jim.service.AdminLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Map;

@RestController
public class AdminLoginController {

    @Autowired
    private AdminLoginService loginService;

    private static final Logger logger = LogManager.getLogger(AdminLoginController.class);
    private static final String LOG_HEADER = "[" + AdminLoginController.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + AdminLoginController.class.getName() + "] :: ";

    @PostMapping("/Adminlogin")
    public ResponseEntity<String> adminLogin(@RequestBody Map<String, String> loginData) {

        String username = loginData.get("username");
        String password = loginData.get("password");

        // Validate the credentials

        if (loginService.checkAdminLogin(username, password) == 1) {
            return ResponseEntity.ok("Login successful!");
        } else {
            logger.info(LOG_HEADER + "received username : " + username + " received password : " + password );
            System.out.println("received username : " + username + " received password : " + password);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed!");
        }
    }
}
