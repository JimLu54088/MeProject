package hideFolder;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class HiddenFolderManager {

    private static final String CONFIG_FILE = "hidden_folders.json";

    public static void main(String[] args) throws Exception {

        System.out.println("Please input password.");
        Scanner scanner = new Scanner(System.in);

        String password = scanner.next();

        if (!password.equals("XXXXXX")) {
            System.out.println("Password WRONG!");
            Thread.sleep(1000);
            return;
        } else {
            while (true) {
                System.out.println("\n選擇操作:");
                System.out.println("1. Lock folder | 2. List locked folder | 3. Unlock folder| 4. Exit");
                System.out.print("Input selection: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 清理緩衝區

                switch (choice) {
                    case 1:
                        System.out.println("Input the folder you want to lock: (Example: C:\\fff\\fff\\fff)");
                        String folderToHide = scanner.nextLine();
                        hideFolder(folderToHide);
                        break;
                    case 2:
                        listHiddenFolders();
                        break;
                    case 3:
                        System.out.println("Input the folder you want to unlock: ");
                        String folderToUnhide = scanner.nextLine();
                        unhideFolder(folderToUnhide);
                        break;
                    case 4:
                        System.out.println("Exit program...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Input not working, try again.");
                }
            }
        }


    }


    // 隱藏資料夾
    public static void hideFolder(String folderPath) throws IOException {
        Path pathfolderPath = Paths.get(folderPath);
        if (Files.exists(pathfolderPath)) {
            if (Files.isDirectory(pathfolderPath)) {
                try {

                    Process process = Runtime.getRuntime().exec("icacls " + folderPath + " /deny Everyone:(F)");
                    process.waitFor();
                    System.out.println("Lock " + folderPath + " Successfully.");
                    // 紀錄到配置文件
                    addFolderToConfig(folderPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                System.out.println(folderPath + " is not a folder.");
            }


        } else {
            System.out.println(folderPath + " doesn't exist.");
        }
    }

    private static void addFolderToConfig(String folderPath) {
        List<String> hiddenFolders = getHiddenFoldersFromConfig();
        if (!hiddenFolders.contains(folderPath)) {
            hiddenFolders.add(folderPath);
            saveHiddenFoldersToConfig(hiddenFolders);
        }
    }

    private static void saveHiddenFoldersToConfig(List<String> hiddenFolders) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(hiddenFolders);


        try (Writer writer = new FileWriter(CONFIG_FILE)) {

            //Encrypt the message
            byte[] encryptedData = EncryptAndDecryptUtil.encrypt(jsonArray.toJSONString());


            writer.write(Base64.getEncoder().encodeToString(encryptedData));
        } catch (Exception e) {
            System.err.println("保存配置文件時發生錯誤: " + e.getMessage());
        }
    }

    private static List<String> getHiddenFoldersFromConfig() {
        List<String> hiddenFolders = new ArrayList<>();
        File configFile = new File(CONFIG_FILE);

        if (configFile.exists()) {
            try (Reader reader = new FileReader(configFile)) {
                // 1. 讀取文件內容到字符串
                StringBuilder fileContent = new StringBuilder();
                char[] buffer = new char[1024];
                int bytesRead;
                while ((bytesRead = reader.read(buffer)) != -1) {
                    fileContent.append(buffer, 0, bytesRead);
                }

                byte[] encryptedData = Base64.getDecoder().decode(fileContent.toString());


                // 2. 解密內容
                String decryptedContent = EncryptAndDecryptUtil.decrypt(encryptedData); // 假設decrypt方法已經實現

                // 3. 將解密的內容解析為 JSON
                JSONParser parser = new JSONParser();
                JSONArray jsonArray = (JSONArray) parser.parse(decryptedContent);

                // 4. 處理 JSON 數據
                for (Object obj : jsonArray) {
                    hiddenFolders.add((String) obj);
                }
            } catch (Exception e) {
                System.err.println("讀取或解密配置文件時發生錯誤: " + e.getMessage());
            }
        }

        return hiddenFolders;
    }

    public static void listHiddenFolders() {
        List<String> hiddenFolders = getHiddenFoldersFromConfig();
        if (hiddenFolders.isEmpty()) {
            System.out.println("目前沒有隱藏的資料夾。");
        } else {
            System.out.println("已隱藏的資料夾:");
            hiddenFolders.forEach(System.out::println);
        }
    }

    public static void unhideFolder(String folderPath) throws IOException {

        try {

            Process process = Runtime.getRuntime().exec("icacls " + folderPath + " /grant Everyone:F");
            process.waitFor();
            // 從配置文件移除
            removeFolderFromConfig(folderPath);
            System.out.println("Unlock " + folderPath + " Successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void removeFolderFromConfig(String folderPath) {
        List<String> hiddenFolders = getHiddenFoldersFromConfig();
        hiddenFolders.remove(folderPath);
        saveHiddenFoldersToConfig(hiddenFolders);
    }


}
