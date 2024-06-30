package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.common.JwtTokenUtil;
import jp.co.jim.service.AdminLoginService;
import jp.co.jim.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.Map;

@RestController
public class AdminLoginController {

    @Autowired
    private AdminLoginService loginService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserActionService userActionService;

    private static final Logger logger = LogManager.getLogger(AdminLoginController.class);
    private static final String LOG_HEADER = "[" + AdminLoginController.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + AdminLoginController.class.getName() + "] :: ";

    @PostMapping("/Adminlogin")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, String> loginData) {

        String username = loginData.get("username");
        String password = loginData.get("password");

        //Output received reuqestBody
        Gson gson = new Gson();
        String jsonLoginData = gson.toJson(loginData);
        logger.info(LOG_HEADER + "received requestBody : " + jsonLoginData);


        // Validate the credentials

        if (loginService.checkAdminLogin(username, password) == 1) {
            // Record successful login action
            userActionService.saveUserAction(username, "Admin Login successful");


            // 验证通过，生成JWT
            try {
                String token = jwtTokenUtil.generateToken(username);

                // 返回JWT给客户端
                return ResponseEntity.ok(Map.of("admintoken", token));
            } catch (Exception e) {
                logger.error(ERROR_LOG_HEADER + "Token getting failed." + e);
                throw new RuntimeException("Token getting failed.");

            }

        } else {
            // Record unsuccessful login action
            userActionService.saveUserAction(username, "Login failed!");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin Login failed!");
        }
    }
}
