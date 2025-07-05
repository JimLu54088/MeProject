package jp.co.jim;

import java.io.File;

public class EncryptDemo {
    public static void main(String[] args) throws Exception {
        File input = new File("D:\\myjava\\PGPKeyGenerator\\testFiles\\iiiiiiiiiii.txt");
        File output = new File("D:\\myjava\\PGPKeyGenerator\\testFiles\\encryptedOutput.pgp");
        File publicKey = new File("D:\\myjava\\PGPKeyGenerator\\keyFiles\\public_key.asc");

        PGPEncryptionUtil.encryptFile(input, output, publicKey, false); // true 表示輸出為 .asc
        System.out.println("✅ 檔案加密完成！");
    }


}
