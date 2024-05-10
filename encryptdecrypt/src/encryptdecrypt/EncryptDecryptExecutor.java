package encryptdecrypt;

public class EncryptDecryptExecutor {
    public static void main(String[] args) {
        switch (args[0]) {
            case "Encrypt":
                Encrypt.encrypt(args[1]);
                break;
            case "Decrypt":
                Decrypt.decrypt(args[1]);
                break;

            default:
                //doNothing
            	System.out.print("");
        }
    }
}
