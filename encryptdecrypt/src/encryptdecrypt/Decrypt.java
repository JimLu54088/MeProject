package encryptdecrypt;

import java.util.Base64;

public class Decrypt {
    public static void decrypt(String encryptedDataTest) {
        try {

            byte[] encryptedData = Base64.getDecoder().decode(encryptedDataTest);

            //Decrypt the message
            String decryptedText = EncryptAndDecryptUtil.decrypt(encryptedData);

            System.out.println("Encrypted Message : " + Base64.getEncoder().encodeToString(encryptedData));
            System.out.println("Decrypted Message : " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
