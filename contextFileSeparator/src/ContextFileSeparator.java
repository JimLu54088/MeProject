import java.io.*;
import java.nio.file.*;
import java.util.*;


public class ContextFileSeparator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("請輸入檔案路徑: ");
        String filePath = scanner.nextLine().trim();

        File inputFile = new File(filePath);
        if (!inputFile.exists()) {
            System.out.println("找不到檔案: " + filePath);
            return;
        }

        System.out.print("請輸入要分割成幾份: ");
        int partCount = scanner.nextInt();

        List<Integer> splitPoints = new ArrayList<>();
        for (int i = 1; i < partCount; i++) {
            System.out.print("請輸入分割點 " + i + "（第幾行為止）: ");
            splitPoints.add(scanner.nextInt());
        }

        System.out.println("開始分割...");

        String baseName = getBaseName(inputFile.getName());
        String ext = getExtension(inputFile.getName());
        Path parentDir = inputFile.toPath().getParent();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            int currentLine = 0;
            int partIndex = 1;
            int nextSplitPoint = splitPoints.isEmpty() ? Integer.MAX_VALUE : splitPoints.get(0);
            int splitIndex = 0;

            BufferedWriter writer = createWriter(parentDir, baseName, ext, partIndex, partCount);

            String line;
            while ((line = reader.readLine()) != null) {
                currentLine++;

                writer.write(line);
                writer.newLine();

                if (currentLine == nextSplitPoint) {
                    writer.close();
                    partIndex++;

                    if (partIndex <= partCount) {
                        writer = createWriter(parentDir, baseName, ext, partIndex, partCount);
                        splitIndex++;
                        nextSplitPoint = (splitIndex < splitPoints.size()) ? splitPoints.get(splitIndex) : Integer.MAX_VALUE;
                    }
                }
            }

            writer.close();
            System.out.println("分割完成，共產生 " + partIndex + " 個檔案。");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedWriter createWriter(Path dir, String baseName, String ext, int index, int total) throws IOException {
        String newFileName = String.format("%s_%d_%d%s", baseName, index, total, ext);
        Path outputPath = dir.resolve(newFileName);
        return Files.newBufferedWriter(outputPath);
    }

    private static String getBaseName(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    private static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }
}