package hideFolder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


public class EncryptAndDecryptUtil {

    private static final String SECRET_KEY = "XXXXXX";

    //Generate a random AES secret key
    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //Define your AES secret key
        byte[] keyBytes = SECRET_KEY.getBytes("UTF-8");
        return new SecretKeySpec(keyBytes, "AES");
    }

    //Encrypt the message using AES
    public static byte[] encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey());
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    //Decrypt the message using AES
    public static String decrypt(byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, generateSecretKey());
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        return new String(decryptedBytes, "UTF-8");

    }


}
