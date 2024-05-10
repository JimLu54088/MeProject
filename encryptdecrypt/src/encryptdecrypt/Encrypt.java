package encryptdecrypt;

import java.util.Base64;

public class Encrypt {

    public static void encrypt(String plainText) {
        try {

            //Encrypt the message
            byte[] encryptedData = EncryptAndDecryptUtil.encrypt(plainText);

            System.out.println("Original Message : " + plainText);
            System.out.println("Encrypted Message : " + Base64.getEncoder().encodeToString(encryptedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
