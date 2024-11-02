package jp.co.jim.controller;

import jp.co.jim.entity.User;
import jp.co.jim.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //取得目前登入的用戶資訊
    @GetMapping("/")
    //從header取得JWT
    public ResponseEntity<User> getUserInfo(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJWT(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}