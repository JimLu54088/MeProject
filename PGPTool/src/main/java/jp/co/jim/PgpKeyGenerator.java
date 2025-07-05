package jp.co.jim;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.jcajce.*;

import java.io.*;
import java.security.*;
import java.util.Date;

public class PgpKeyGenerator {

    public static void generateKeyPair(String identity, String passphrase,
                                       OutputStream publicOut, OutputStream privateOut) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
        kpg.initialize(2048); // or 4096
        KeyPair kp = kpg.generateKeyPair();

        PGPDigestCalculator sha1Calc = new JcaPGPDigestCalculatorProviderBuilder()
                .build().get(HashAlgorithmTags.SHA1);

        PGPKeyPair pgpKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, kp, new Date());

        PBESecretKeyEncryptor secretKeyEncryptor =
                new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.AES_256, sha1Calc)
                        .setProvider("BC")
                        .build(passphrase.toCharArray());

        PGPKeyRingGenerator keyRingGen = new PGPKeyRingGenerator(
                PGPSignature.POSITIVE_CERTIFICATION,
                pgpKeyPair,
                identity,
                sha1Calc,
                null,
                null,
                new JcaPGPContentSignerBuilder(pgpKeyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1),
                secretKeyEncryptor
        );

        // 輸出公鑰（.asc）
        try (ArmoredOutputStream pubOut = new ArmoredOutputStream(publicOut)) {
            keyRingGen.generatePublicKeyRing().encode(pubOut);
        }

        // 輸出私鑰（.asc）
        try (ArmoredOutputStream secOut = new ArmoredOutputStream(privateOut)) {
            keyRingGen.generateSecretKeyRing().encode(secOut);
        }
    }

    // 測試用 main
    public static void main(String[] args) throws Exception {
        try (
                FileOutputStream pubOut = new FileOutputStream("D:\\myjava\\PGPKeyGenerator\\keyFiles\\public_key.asc");
                FileOutputStream privOut = new FileOutputStream("D:\\myjava\\PGPKeyGenerator\\keyFiles\\private_key.asc")
        ) {
            generateKeyPair("Jim <jim@example.com>", "#p]53i~uDw>UW0K8]uDV", pubOut, privOut);
        }

        System.out.println("✅ PGP Key pair generated!");
    }



}
