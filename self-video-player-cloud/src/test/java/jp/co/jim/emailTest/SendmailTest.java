package jp.co.jim.emailTest;

import jp.co.jim.util.EmailUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SendmailTest {

    @Test
    void sendMailTest() throws Exception {

        Map<String, String> smtpDetails = new HashMap<>();
        smtpDetails.put("smtpServerAddress", "XXXXXXX.jp");
        smtpDetails.put("email_port", "587");
        smtpDetails.put("email_auth", "true");
        smtpDetails.put("emailsetFrom", "XXXXXXXXXXX.jp");
        smtpDetails.put("email_password", "XXXXXXXXXXXXX");

        EmailUtil.sendEmail("AAAA@gmail.com,DDDD@gmail.com","email sending test","test",smtpDetails);

        System.out.println("test");
    }


}
