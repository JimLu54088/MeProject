package jp.co.jim.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JWTProvider {
    //產生加密JWT的密鑰
    SecretKey key = Keys.hmacShaKeyFor(JWTConstant.SECRET != null ? JWTConstant.SECRET.getBytes() : null);

    @Value("${tokenExpiredMin}")
    private long VALIDITY_DURATION_MIN;

    //設定產生的token的內容
    public String generateToken(String email) {
        return Jwts.builder()
                //夾帶token發行時間
                .setIssuedAt(new Date())
                //夾帶token到期時間
                .setExpiration(new Date(new Date().getTime() + VALIDITY_DURATION_MIN * 60 * 1000))
                //夾帶email地址
                .claim("email", email)
                //用密鑰加密
                .signWith(key)
                //轉換成字串
                .compact();
    }

    public String getEmailFromJWT(String jwt) {
        jwt = jwt.substring("Bearer ".length() - 1);
        //取得JWT payload的內容
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        return String.valueOf(claims.get("email"));
    }
}
