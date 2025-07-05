package jp.co.jim;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DecryptDemo {

    public static void main(String[] args) {
        // === 路徑設定 ===
        String encryptedFilePath = "D:\\myjava\\PGPKeyGenerator\\testFiles\\encryptedOutput.pgp"; // 你的加密檔
        String privateKeyPath = "D:\\myjava\\PGPKeyGenerator\\keyFiles\\private_key.asc";            // 你的私鑰檔
        String passphrase = "#p]53i~uDw>UW0K8]uDV";                             // 私鑰密碼
        String outputFilePath = "D:\\myjava\\PGPKeyGenerator\\testFiles\\descrptOutput.txt";       // 解密後輸出檔案

        // === 解密流程 ===
        try (
                InputStream encryptedInput = new FileInputStream(encryptedFilePath);
                InputStream privateKeyInput = new FileInputStream(privateKeyPath);
                OutputStream decryptedOutput = new FileOutputStream(outputFilePath)
        ) {
            PGPDecryptionUtil.decryptFile(encryptedInput, privateKeyInput, passphrase, decryptedOutput);
            System.out.println("✅ 解密成功！輸出檔案： " + outputFilePath);
        } catch (Exception e) {
            System.err.println("❌ 解密失敗：" + e.getMessage());
            e.printStackTrace();
        }
    }



}
