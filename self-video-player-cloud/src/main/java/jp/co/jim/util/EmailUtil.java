package jp.co.jim.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class EmailUtil {

    private static final Logger logger = (Logger) LogManager.getLogger(EmailUtil.class);

    public static void sendEmail(String toEmail, String subject,
                                 String body, Map<String, String> smtpDetails) {

        String host = smtpDetails.get("smtpServerAddress");
        String port = smtpDetails.get("email_port");
        String auth = smtpDetails.get("email_auth"); // "true" / "false"
        String username = smtpDetails.get("emailsetFrom");
        String password = smtpDetails.get("email_password");

        logger.info("Preparing email...");

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", auth);

            // ✅ 自動決定使用 TLS 或 SSL
            if ("465".equals(port)) {
                // SSL
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.ssl.trust", host);
            } else {
                // STARTTLS
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.ssl.trust", host);
            }

            // 建立 Session（帶登入資訊）
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // 想查看 SMTP 連線過程 → 打開 debug（非常有用）
            // session.setDebug(true);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            message.setSubject(subject, "UTF-8");
            message.setSentDate(new Date());

            // 正文（HTML）
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(body, "text/html; charset=UTF-8");

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            // ✅ 必須 setContent（你之前漏掉）
            message.setContent(multipart);

            logger.info("Sending email to {}", toEmail);
            Transport.send(message);
            logger.info("Email sent successfully!");

        } catch (Exception e) {
            logger.error("Email sending failed: ", e);
        }
    }
}
