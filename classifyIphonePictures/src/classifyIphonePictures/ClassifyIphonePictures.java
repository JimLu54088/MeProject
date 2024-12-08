package classifyIphonePictures;

import classifyIphonePictures.common.PropertyLoader;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.File;

public class ClassifyIphonePictures {
    public static void main(String[] args) {

        System.out.println("\n----------Process started ------------\n" + new Date());
        try {
            if (args.length == 0 || args[0] == null || args[0].isEmpty()) {
                System.err.println(
                        "ERROR: Property File Name is not given. Please provide info -Dproperty.file=<Path of property file>");
                System.exit(-1);
            }

            String sPropertyFile = args[0];
            PropertyLoader.propertyFilePath = sPropertyFile;

            System.out.println(sPropertyFile);

            // 原始資料夾路徑
            String download_from_iCloudDir = PropertyLoader.gerProperty("download_from_iCloud");
            // 目標資料夾路徑
            String taken_from_iPhone_onlyDir = PropertyLoader.gerProperty("taken_from_iPhone_only");
            //Time different in hour
            String timeDiff_in_hour = PropertyLoader.gerProperty("time_diff_in_hour");

            System.out.println("download_from_iCloudDir :: " + download_from_iCloudDir);
            System.out.println("taken_from_iPhone_onlyDir :: " + taken_from_iPhone_onlyDir);
            System.out.println("time_diff_in_hour :: " + timeDiff_in_hour);

            int int_TimeDiff_in_hour = Integer.parseInt(timeDiff_in_hour);

            // 建立目標資料夾
            File taken_from_iPhone_onlyFolder = new File(taken_from_iPhone_onlyDir);
            if (!taken_from_iPhone_onlyFolder.exists()) {
                taken_from_iPhone_onlyFolder.mkdirs();
            }

            try {
                // 遍歷原始資料夾的所有檔案
                Files.list(new File(download_from_iCloudDir).toPath())
                        .filter(Files::isRegularFile) // 過濾非檔案
                        .forEach(file -> {
                            try {
                                // 判斷是否為 HEIF 格式
                                if (isHeifImage(file.toFile())) {
                                    // 生成新檔名
                                    String newFileName = generateNewFileName(file.toFile(), int_TimeDiff_in_hour);
                                    Path targetPath = Path.of(taken_from_iPhone_onlyDir, newFileName);

                                    // 複製檔案並重新命名
                                    Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                                    System.out.println("Copy and rename file: " + file.getFileName() + " -> " + newFileName);
                                }
                            } catch (Exception e) {
                                System.err.println("Error while processing file: " + file.getFileName() + " - " + e.getMessage());
                            }
                        });
            } catch (IOException e) {
                System.err.println("Error while reading file from folder: " + e.getMessage());
            }


            System.out.println("----------Process finished ------------\n" + new Date());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static boolean isHeifImage(File file) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            // 判斷是否包含 ExifSubIFDDirectory
            if (metadata.containsDirectoryOfType(ExifSubIFDDirectory.class)) {
                System.out.println("File: " + file.getName());

                // 取得 Exif 資訊
                ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                if (exifDirectory != null) {
                    // 打印相機資訊
                    String cameraMake = exifDirectory.getString(ExifSubIFDDirectory.TAG_MAKE); // 相機製造商
                    String cameraModel = exifDirectory.getString(ExifSubIFDDirectory.TAG_MODEL); // 相機型號
                    Date captureDate = exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL); // 拍攝日期

                    System.out.println("  Camera maker: " + (cameraMake != null ? cameraMake : "unknown"));
                    System.out.println("  Camera type: " + (cameraModel != null ? cameraModel : "unknown"));
                    System.out.println("  capture date: " + (captureDate != null ? captureDate : "unknown"));

                    //For Screen shots images
                    BufferedImage image = ImageIO.read(file);

                    // 獲取圖片的寬度和高度
                    int width = image.getWidth();
                    int height = image.getHeight();

                    String strScreenShotsWidth = PropertyLoader.gerProperty("screenShotsWidth");
                    String strScreenShotsHeight = PropertyLoader.gerProperty("screenShotsHeight");

                    int intScreenShotsWidth = Integer.parseInt(strScreenShotsWidth);
                    int intScreenShotsHeight = Integer.parseInt(strScreenShotsHeight);

                    // 打印圖片尺寸
//                    System.out.println("圖片尺寸: " + width + " x " + height);

                    //For screen shots impages not copy to target Folder
                    if (width == intScreenShotsWidth && height == intScreenShotsHeight) {
                        System.out.println(file.getName() + " may be screenShots and will not copy to target folder.");
                        return false;
                    }


                    return captureDate != null;

                }
            } else {
                System.out.println("File " + file.getName() + " doesn't contain EXIF information.");
            }
        } catch (Exception e) {
            System.err.println("Cannot read Metadata: " + file.getName() + " - " + e.getMessage());
        }
        return false;
    }


    // 生成新檔名（根據拍照時間或修改時間）
    private static String generateNewFileName(File file, int time_diff_in_hour) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifSubIFDDirectory exif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            Date captureDate = null;

            // 優先使用拍照時間
            if (exif != null) {
                captureDate = exif.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            }

            // 如果拍照時間不可用，則使用最後修改時間
            if (captureDate == null) {
                captureDate = new Date(file.lastModified());
            }

            // 使用 Calendar 減去 9 小時
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(captureDate);
            calendar.add(Calendar.HOUR_OF_DAY, time_diff_in_hour);
            captureDate = calendar.getTime();

            // 格式化時間為 yyyyMMdd_HHmmss
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String formattedDate = sdf.format(captureDate);

            // 取得原始檔案副檔名
            String extension = getFileExtension(file.getName());

            return formattedDate + (extension.isEmpty() ? "" : "." + extension);
        } catch (Exception e) {
            System.err.println("Error while generate new file name: " + file.getName() + " - " + e.getMessage());
        }

        // 預設檔名為原始名稱
        return file.getName();
    }

    // 取得檔案的副檔名
    private static String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return (lastIndex > 0) ? fileName.substring(lastIndex + 1) : "";
    }


}


