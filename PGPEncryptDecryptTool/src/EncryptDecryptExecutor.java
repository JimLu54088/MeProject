import com.PropertyLoader;

public class EncryptDecryptExecutor {
    public static void main(String[] args) throws Exception {
        switch (args[0]) {
            case "EncryptPGP":
                PropertyLoader.propertyFilePath = args[1];
                String inputFile = PropertyLoader.gerProperty("inputFile");
                String outputFile = PropertyLoader.gerProperty("outputFile");
                String keyFile = PropertyLoader.gerProperty("keyFile");
                PGPEncrypt.encrypt(inputFile, outputFile, keyFile);
                break;
            case "DecryptPGP":
                PropertyLoader.propertyFilePath = args[1];
                inputFile = PropertyLoader.gerProperty("inputFile");
                outputFile = PropertyLoader.gerProperty("outputFile");
                keyFile = PropertyLoader.gerProperty("keyFile");
                String keyPass = PropertyLoader.gerProperty("keyPass");
                PGPDecrypt.decrypt(inputFile,outputFile, keyFile, keyPass);
                break;

            default:
                //doNothing
                System.out.print("");
        }
    }
}
