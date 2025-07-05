package jp.co.jim;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.jcajce.*;
import java.io.*;
import java.security.Security;
import java.util.Iterator;

public class PGPDecryptionUtil {

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * 解密 PGP 加密的檔案。
     *
     * @param encryptedInput 加密輸入流（可為檔案流）
     * @param privateKeyInput 私鑰輸入流
     * @param passphrase 私鑰密碼
     * @param outputStream 解密後輸出流（可為檔案流）
     * @throws IOException
     * @throws PGPException
     */
    public static void decryptFile(
            InputStream encryptedInput,
            InputStream privateKeyInput,
            String passphrase,
            OutputStream outputStream
    ) throws IOException, PGPException {

        InputStream decoderStream = PGPUtil.getDecoderStream(encryptedInput);
        PGPObjectFactory pgpFactory = new PGPObjectFactory(decoderStream, new JcaKeyFingerprintCalculator());

        Object obj = pgpFactory.nextObject();
        if (!(obj instanceof PGPEncryptedDataList)) {
            obj = pgpFactory.nextObject(); // 嘗試下一個物件
        }

        if (!(obj instanceof PGPEncryptedDataList edl)) {
            throw new IllegalArgumentException("Encrypted data not found.");
        }

        // 讀取私鑰集
        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
                PGPUtil.getDecoderStream(privateKeyInput),
                new JcaKeyFingerprintCalculator()
        );

        PGPPrivateKey privateKey = null;
        PGPPublicKeyEncryptedData encryptedData = null;

        // 找到與私鑰匹配的加密資料
        for (Iterator<PGPEncryptedData> it = edl.getEncryptedDataObjects(); it.hasNext(); ) {
            PGPEncryptedData ed = it.next();
            if (ed instanceof PGPPublicKeyEncryptedData pked) {
                PGPSecretKey secretKey = pgpSec.getSecretKey(pked.getKeyID());
                if (secretKey != null) {
                    privateKey = secretKey.extractPrivateKey(
                            new JcePBESecretKeyDecryptorBuilder()
                                    .setProvider("BC")
                                    .build(passphrase.toCharArray())
                    );
                    encryptedData = pked;
                    break;
                }
            }
        }

        if (privateKey == null || encryptedData == null) {
            throw new IllegalArgumentException("Secret key for decryption not found.");
        }

        // 這裡修正成 PublicKeyDataDecryptorFactory，不要轉型成 PBEDataDecryptorFactory
        PublicKeyDataDecryptorFactory dataDecryptorFactory = new JcePublicKeyDataDecryptorFactoryBuilder()
                .setProvider("BC")
                .build(privateKey);

        InputStream clearDataStream = encryptedData.getDataStream(dataDecryptorFactory);
        PGPObjectFactory clearFactory = new PGPObjectFactory(clearDataStream, new JcaKeyFingerprintCalculator());
        Object message = clearFactory.nextObject();

        if (message instanceof PGPCompressedData compressedData) {
            clearFactory = new PGPObjectFactory(compressedData.getDataStream(), new JcaKeyFingerprintCalculator());
            message = clearFactory.nextObject();
        }

        if (message instanceof PGPLiteralData literalData) {
            try (InputStream dataIn = literalData.getInputStream()) {
                dataIn.transferTo(outputStream);
            }
        } else {
            throw new PGPException("Unsupported PGP message format: " + message.getClass());
        }

        if (encryptedData.isIntegrityProtected() && !encryptedData.verify()) {
            throw new PGPException("Message failed integrity check");
        }
    }
}
