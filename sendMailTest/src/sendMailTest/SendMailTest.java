package sendMailTest;


import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMailTest {
    public static void main(String[] args) {
        try {

            String[] recipients = {"xxx@gmail.com", "xxx@gmail.com"};

            String content ="\n" +
                    "     ***********                  ***********\n" +
                    "  *****************            *****************\n" +
                    "*********************        *********************\n" +
                    "***********************      ***********************\n" +
                    "************************    ************************\n" +
                    "*************************  *************************\n" +
                    " **************************************************\n" +
                    "  ************************************************\n" +
                    "    ********************************************\n" +
                    "      ****************************************\n" +
                    "         **********************************\n" +
                    "           ******************************\n" +
                    "              ************************\n" +
                    "                ********************\n" +
                    "                   **************\n" +
                    "                     **********\n" +
                    "                       ******\n" +
                    "                         **";



            sendEmail(recipients, "Love you!!", content);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendEmail(String[] recipients, String subject, String content) throws MessagingException {
        // 設置郵件屬性
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "csii.sakura.ne.jp"); // Sakura 的 SMTP 伺服器
        properties.put("mail.smtp.port", "587"); // SMTP 埠，根據伺服器設置
        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true"); // 如果需要 TLS
        // 也可以用 SSL，則設置 mail.smtp.ssl.enable = true

        // 建立認證
        String myAccountEmail = "xxxx@csii.jp";
        String password = "xxxx";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        // 建立郵件內容
        Message message = prepareMessage(session, myAccountEmail, recipients, subject, content);

        // 發送郵件
        Transport.send(message);
        System.out.println("郵件已發送成功！");
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String[] recipients, String subject, String content) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));

            // 添加多個收件人
            InternetAddress[] recipientAddresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                recipientAddresses[i] = new InternetAddress(recipients[i]);
            }
            message.setRecipients(Message.RecipientType.TO, recipientAddresses); // 可以使用 TO, CC, BCC

            message.setSubject(subject);
            message.setText(content);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
