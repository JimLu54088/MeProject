package sendMailTest;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMailTestWithPicture {

    public static void main(String[] args) {
        try {

            String[] recipients = {"xxxx@gmail.com", "xxxx@gmail.com"};

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendEmail(String[] recipients, String subject, String content) throws Exception {
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

        // 構建郵件
        Message message = prepareMessage(session, myAccountEmail, recipients);

        // 發送郵件
        Transport.send(message);
        System.out.println("郵件已發送成功！");
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String[] recipients) throws Exception {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(myAccountEmail));
        // 添加多個收件人
        InternetAddress[] recipientAddresses = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            recipientAddresses[i] = new InternetAddress(recipients[i]);
        }
        message.setRecipients(Message.RecipientType.TO, recipientAddresses); // 可以使用 TO, CC, BCC
        message.setSubject("這是一封帶有內嵌圖片的郵件");

        // 構建消息的 HTML 部分
        String msg = "<h1>這是一封包含圖片的郵件</h1>"
                + "<img src=\"cid:image1\">";  // 使用 cid 來引用嵌入的圖片

        // 創建 MIME 多部分消息
        MimeMultipart multipart = new MimeMultipart("related");

        // 第一部分：HTML 內容
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(msg, "text/html");
        multipart.addBodyPart(messageBodyPart);

        // 第二部分：圖片附件
        messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource("C:/Users/Jim/Downloads/sendMailTemplate/heart.png"); // 替換為圖片的路徑
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID", "<image1>");  // cid 必須匹配 HTML 中的引用
        multipart.addBodyPart(messageBodyPart);

        // 將多部分消息設置為郵件內容
        message.setContent(multipart);

        return message;
    }



}
