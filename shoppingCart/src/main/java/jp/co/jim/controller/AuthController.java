package jp.co.jim.controller;

import jakarta.annotation.PostConstruct;
import jp.co.jim.bean.utils.EmailUtil;
import jp.co.jim.config.JWTProvider;
import jp.co.jim.entity.Cart;
import jp.co.jim.entity.User;
import jp.co.jim.service.CartService;
import jp.co.jim.service.UserService;
import jp.co.jim.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JWTProvider jwtProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;

    @Value("#{${smtpDetails}}")
    private Map<String, String> smtpDetails;

    @Value("${templatesDirectory}")
    private String templatesDirectory;

    @Value("${emailSubjectForSignUp}")
    private String emailSubjectForSignUp;

    @Autowired
    private TemplateEngine templateEngine;

    public AuthController(JWTProvider jwtProvider, UserService userService, PasswordEncoder passwordEncoder, CartService cartService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
    }

    @PostConstruct
    public void init() {
        setTemplateResolver();
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        //處理用戶註冊
        userService.createUser(user);

        //使用JWTProvider產生token
        String token = jwtProvider.generateToken(user.getEmail());

        //Get userId from Email
        String userId = userService.findUserByEmail(user.getEmail()).getId().toString();
        System.out.println("userid is ::" + userId);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signup Success");
        authResponse.setUserId(userId);

        //建立用戶時，也建立購物車
        Cart cart = cartService.createCart(userService.findUserByEmail(user.getEmail()));

        //send email
        Context context = new Context();

//        setTemplateResolver();
        context.setVariable("emailAddress", user.getEmail());
        context.setVariable("userID",userId);

        if(null == smtpDetails || smtpDetails.isEmpty()){
            throw new RuntimeException("smtp Details information not found in config.");
        }
        String toEmail = user.getEmail();
        String subject = new String(emailSubjectForSignUp.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        System.out.println("email subject for signup :: " + subject);
        String mailTemplatePath = "signUp";

        String emailContent = templateEngine.process(mailTemplatePath, context);
        EmailUtil.sendEmail(toEmail, subject, emailContent, smtpDetails, null);

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody User user) throws Exception {
        //根據提供的email在資料庫中找尋用戶
        User foundUser = userService.findUserByEmail(user.getEmail());

        //如果找不到或密碼不對，就顯示錯誤
        if(foundUser == null || !passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            throw new Exception("Invalid email or password");
        }

        //產生token
        String token = jwtProvider.generateToken(foundUser.getEmail());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Success");
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private void setTemplateResolver() {
        FileTemplateResolver templateResolver = new FileTemplateResolver ();
        templateResolver.setPrefix(templatesDirectory);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setOrder(templateEngine.getTemplateResolvers().size());
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        templateResolver.setCheckExistence(true);
        templateEngine.setTemplateResolver(templateResolver);
    }

}
