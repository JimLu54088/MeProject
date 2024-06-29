package jp.co.jim.controller;

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

    private static final Logger logger = LogManager.getLogger(AdminLoginController.class);
    private static final String LOG_HEADER = "[" + AdminLoginController.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + AdminLoginController.class.getName() + "] :: ";

    @PostMapping("/Adminlogin")
    public ResponseEntity<String> adminLogin(@RequestBody Map<String, String> loginData) {

        // 手動指定配置文件路徑
        Configurator.initialize(null, "file:///C:/pleiades/eclipse/workspace3/loginScreen/src/main/resources/log4j2.xml");


        String username = loginData.get("username");
        String password = loginData.get("password");

        // Validate the credentials (this is just an example, replace with real validation)
        if ("abc".equals(username) && "123".equals(password)) {
            return ResponseEntity.ok("Login successful!");
        } else {
            logger.info(LOG_HEADER + "received username : " + username + " received password : " + password );
            System.out.println("received username : " + username + " received password : " + password);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed!");
        }
    }
}
