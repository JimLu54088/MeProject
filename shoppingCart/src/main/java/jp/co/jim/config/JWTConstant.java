package jp.co.jim.config;

import io.github.cdimascio.dotenv.Dotenv;

public class JWTConstant {
    static Dotenv dotenv = Dotenv.load();
    public static final String SECRET = dotenv.get("JWT_CONSTANT");
}
