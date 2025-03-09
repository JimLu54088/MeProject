import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.util.Iterator;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.util.io.Streams;

public class PGPDecrypt {

    public static void decrypt(String inputFileName, String outputFileName, String keyFileName, String keyPass) throws SignatureException, PGPException, IOException {

        Security.addProvider(new BouncyCastleProvider());
        Security.setProperty("crypto.policy", "unlimited");
        Security.setProperty("provider.10", "org.bouncycastle.jce.provider.BouncyCastleProvider");

        char pass[] = keyPass.toCharArray();

        InputStream in = new BufferedInputStream(new FileInputStream(inputFileName));
        InputStream keyIn = new BufferedInputStream(new FileInputStream(keyFileName));
        try {
            decryptFile(inputFileName, keyFileName, pass, outputFileName);
            keyIn.close();
            in.close();
        } catch (NoSuchProviderException | IOException e) {
            e.printStackTrace();
            keyIn.close();
            in.close();
        }
    }


    public static void decryptFile(
            String inputFileName,
            String keyFileName,
            char[] passwd,
            String defaultFileName)
            throws IOException, NoSuchProviderException {
        InputStream in = new BufferedInputStream(new FileInputStream(inputFileName));
        InputStream keyIn = new BufferedInputStream(new FileInputStream(keyFileName));
        decryptFile(in, keyIn, passwd, defaultFileName);
        keyIn.close();
        in.close();
    }

    public static void decryptFile(
            InputStream in,
            InputStream keyIn,
            char[] passwd,
            String defaultFileName)
            throws IOException, NoSuchProviderException {

        in = PGPUtil.getDecoderStream(in);

        try {
            // 解析 PGP 加密數據
            PGPObjectFactory pgpF = new PGPObjectFactory(in, new JcaKeyFingerprintCalculator());
            PGPEncryptedDataList enc;

            Object o = pgpF.nextObject();
            if (o instanceof PGPEncryptedDataList) {
                enc = (PGPEncryptedDataList) o;
            } else {
                enc = (PGPEncryptedDataList) pgpF.nextObject();
            }

            // 解析 Secret Key
            PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
                    PGPUtil.getDecoderStream(keyIn), new JcaKeyFingerprintCalculator()
            );

            Iterator<?> it = enc.getEncryptedDataObjects();
            PGPPrivateKey sKey = null;
            PGPPublicKeyEncryptedData pbe = null;

            while (sKey == null && it.hasNext()) {
                pbe = (PGPPublicKeyEncryptedData) it.next();
                sKey = findSecretKey(pgpSec, pbe.getKeyID(), passwd);
            }

            if (sKey == null) {
                throw new IllegalArgumentException("Secret key for message not found.");
            }

            InputStream clear = pbe.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BC").build(sKey));

            // 解析解密後的 PGP 數據
            PGPObjectFactory plainFact = new PGPObjectFactory(clear, new JcaKeyFingerprintCalculator());

            Object message = plainFact.nextObject();
            boolean isDataFound = false;

            while (message != null) {
                if (message instanceof PGPCompressedData) {
                    PGPCompressedData compressedData = (PGPCompressedData) message;
                    plainFact = new PGPObjectFactory(compressedData.getDataStream(), new JcaKeyFingerprintCalculator());
                    message = plainFact.nextObject();
                }

                if (message instanceof PGPLiteralData) {
                    String outFileName = defaultFileName;
                    PGPLiteralData ld = (PGPLiteralData) message;
                    InputStream unc = ld.getInputStream();
                    OutputStream fOut = new BufferedOutputStream(new FileOutputStream(outFileName));
                    Streams.pipeAll(unc, fOut);
                    fOut.close();
                    isDataFound = true;
                }

                message = plainFact.nextObject();
            }

            if (!isDataFound) {
                throw new PGPException("PGP Literal Data not found.");
            }

            if (pbe.isIntegrityProtected()) {
                if (!pbe.verify()) {
                    System.err.println("Message failed integrity check");
                } else {
                    System.err.println("Message integrity check passed");
                }
            } else {
                System.err.println("No message integrity check");
            }

        } catch (PGPException e) {
            System.err.println(e);
            if (e.getUnderlyingException() != null) {
                e.getUnderlyingException().printStackTrace();
            }
        }
    }

    static PGPPrivateKey findSecretKey(PGPSecretKeyRingCollection pgpSec, long keyID, char[] pass)
            throws PGPException {

        PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);

        if (pgpSecKey == null) {
            return null;
        }

        // 創建 PBESecretKeyDecryptor 來解密私鑰
        PBESecretKeyDecryptor decryptor = new JcePBESecretKeyDecryptorBuilder()
                .setProvider("BC")  // 指定 Bouncy Castle 提供者
                .build(pass);        // 使用密碼建立解密器

        return pgpSecKey.extractPrivateKey(decryptor);
    }

}