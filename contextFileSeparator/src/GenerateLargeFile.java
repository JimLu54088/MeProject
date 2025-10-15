import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class GenerateLargeFile {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("請輸入要產生的檔案路徑（例如 C:\\\\temp\\\\test.txt）: ");
        String filePath = scanner.nextLine().trim();

        System.out.print("請輸入要產生的行數（例如 1000000）: ");
        long totalLines = scanner.nextLong();

        Path outputPath = Paths.get(filePath);

        System.out.println("開始產生檔案...");
        long startTime = System.currentTimeMillis();

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            for (long i = 1; i <= totalLines; i++) {
                writer.write(Long.toString(i));
                writer.newLine();

                // 每寫 1 百萬行提示一次進度（可自行調整）
                if (i % 1_000_000 == 0) {
                    System.out.println("已產生 " + i + " 行...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("完成！總行數: " + totalLines + " 行，耗時: " + (elapsed / 1000.0) + " 秒");
    }
}