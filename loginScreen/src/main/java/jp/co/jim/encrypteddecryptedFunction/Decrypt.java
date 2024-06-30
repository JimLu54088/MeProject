package jp.co.jim.encrypteddecryptedFunction;

import java.util.Base64;

public class Decrypt {
    public String decrypt(String encryptedDataTest) {
        String decryptedText = null;
        try {

            byte[] encryptedData = Base64.getDecoder().decode(encryptedDataTest);

            //Decrypt the message
            decryptedText = EncryptAndDecryptUtil.decrypt(encryptedData);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
}