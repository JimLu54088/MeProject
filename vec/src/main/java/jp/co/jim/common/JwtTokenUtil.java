package jp.co.jim.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class JwtTokenUtil {

	private static final Logger logger = LogManager.getLogger(JwtTokenUtil.class);
	private static final String LOG_HEADER = "[" + JwtTokenUtil.class.getSimpleName() + "] :: ";
	private static final String ERROR_LOG_HEADER = "[" + JwtTokenUtil.class.getName() + "] :: ";

	private static final String SECRET_KEY = "3eafd25832b93e9d679a5fb7c0725tyua80c38a6a5ee11ec9388e5ef288f26d5";
	@Value("${tokenExpiredMin}")
	private long VALIDITY_DURATION_MIN; // 30 minutes

	public String generateToken(String username) {
		LocalDateTime localDateTimeNow = LocalDateTime.now();
		LocalDateTime validity = localDateTimeNow.plusMinutes(VALIDITY_DURATION_MIN); // 添加30分钟
		String validityString = validity.format(Constants.YYYYMMDDHHMMSS_FORMATTER);

		logger.info(LOG_HEADER + "Expired at : " + validityString);

		String jwtsToken = Jwts.builder().setSubject(username).setIssuedAt(localDatimeToDate(localDateTimeNow))
				.setExpiration(localDatimeToDate(validity)).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();

		logger.info(LOG_HEADER + "Got token : " + jwtsToken);

		return jwtsToken;
	}

	public static String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

	public static boolean isTokenValid(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Date localDatimeToDate(LocalDateTime localDateTime) {
		ZoneId zone = ZoneId.systemDefault();
		ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zone);
		Instant instant = zonedDateTime.toInstant();
		return Date.from(instant);
	}

}
