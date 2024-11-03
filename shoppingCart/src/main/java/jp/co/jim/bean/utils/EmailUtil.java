package jp.co.jim.bean.utils;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    public static void sendEmail(String toEmail, String subject,
                                 String body, Map<String, String> smtpDetails, File attachment){
        try
        {
            String[] recipients = {toEmail};
            String smtpServerAddress = smtpDetails.get("smtpServerAddress");
            String port = smtpDetails.get("email_port");
            String auth = smtpDetails.get("email_auth");

            Properties props = System.getProperties();
            props.put("mail.smtp.host", smtpServerAddress);
            if(null != port && !port.isEmpty()){
                props.put("mail.smtp.port", port);
            }
            if(null != auth && !auth.isEmpty()){
                props.put("mail.smtp.auth", auth);
            }

            // 建立認證
            String myAccountEmail = smtpDetails.get("emailsetFrom");
            String password = smtpDetails.get("email_password");
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myAccountEmail, password);
                }
            });
            Multipart multipart = new MimeMultipart();

            // 建立郵件內容
            MimeMessage message = new MimeMessage(session);
            //set message headers
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setSubject(subject, "UTF-8");
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body,  "text/html; charset=UTF-8");
            multipart.addBodyPart(messageBodyPart);
            message.setSentDate(new Date());
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toEmail, false));

            if(attachment != null){
                System.out.println("have attachement.");
            }else{

                //setContent
                message.setContent(multipart, "text/html");
            }

            // 發送郵件
            Transport.send(message);
            System.out.println("郵件已發送成功！");


        }
        catch (Exception e) {
            System.out.println("Error while sending mail.");
        }
    }




}
