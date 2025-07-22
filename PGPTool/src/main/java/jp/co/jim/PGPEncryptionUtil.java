package jp.co.jim;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;

import java.io.*;
import java.security.Security;

public class PGPEncryptionUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密一個檔案，輸出為 PGP 加密檔 (.pgp 或 .asc)
     */
    public static void encryptFile(File inputFile, File outputFile, File publicKeyFile, boolean armor) throws Exception {

        OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));
        if (armor) {
            out = new ArmoredOutputStream(out); // 產出 .asc 檔
        }

        PGPPublicKey encKey = readPublicKey(publicKeyFile);

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);

        try (OutputStream cos = comData.open(bOut)) {
            PGPUtil.writeFileToLiteralData(cos, PGPLiteralData.BINARY, inputFile);
        }

        PGPEncryptedDataGenerator encryptedDataGenerator =
                new PGPEncryptedDataGenerator(
                        new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5)
                                .setWithIntegrityPacket(true)
                                .setSecureRandom(new java.security.SecureRandom())
                                .setProvider("BC")
                );

        encryptedDataGenerator.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encKey).setProvider("BC"));

        try (OutputStream encryptedOut = encryptedDataGenerator.open(out, bOut.toByteArray().length)) {
            encryptedOut.write(bOut.toByteArray());
        }

        out.close();
    }

    /**
     * 讀取 .asc or .pgp 公鑰，回傳 PGPPublicKey
     */
    public static PGPPublicKey readPublicKey(File publicKeyFile) throws IOException, PGPException {
        try (InputStream keyIn = new BufferedInputStream(new FileInputStream(publicKeyFile))) {
            PGPPublicKeyRingCollection keyRingCollection = new PGPPublicKeyRingCollection(
                    PGPUtil.getDecoderStream(keyIn),
                    new JcaKeyFingerprintCalculator());

            for (PGPPublicKeyRing keyRing : keyRingCollection) {
                for (PGPPublicKey key : keyRing) {
                    if (key.isEncryptionKey()) {
                        return key;
                    }
                }
            }
        }
        throw new IllegalArgumentException("找不到加密用的公鑰");
    }
}