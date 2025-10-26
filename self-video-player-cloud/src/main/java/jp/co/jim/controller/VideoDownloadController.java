package jp.co.jim.controller;

import jp.co.jim.util.EmailUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/videoDownload")
public class VideoDownloadController {

    private static final Logger logger = (Logger) LogManager.getLogger(VideoDownloadController.class);


    @Value("${ffmpegPath}")
    private String ffmpegPath;

    @Value("${m3u8downloaderEXEPath}")
    private String m3u8downloaderEXEPath;

    @Value("${videoPath}")
    private String downloadDir;

    @Value("#{${smtpDetails}}")
    private Map<String, String> smtpDetails;

    @Value("#{${mailInfo}}")
    private Map<String, Object> mailInfo;




    @PostMapping("/download")
    public ResponseEntity<Void> downloadVideo(@RequestBody Map<String, String> payload) {
        String videoUrl = payload.get("videoUrl");
        String videoName = payload.get("videoName");

        String toEmail = Objects.toString(mailInfo.get("toEmail"), "");

        logger.info("影片網址: " + videoUrl);
        logger.info("影片名稱: " + videoName);


        // 用新 Thread 非同步執行 exe
        new Thread(() -> {
            ProcessBuilder pb = new ProcessBuilder(
                    m3u8downloaderEXEPath,
                    videoUrl,
                    downloadDir,
                    videoName,
                    ffmpegPath
            );
            pb.redirectErrorStream(true);

            try {
                Process process = pb.start();

                // 讀取 exe 輸出（可選）
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info(line);
                }

                int exitCode = process.waitFor();
                logger.info("Execute m3u8 downloader completedly, exec return code： " + exitCode);
                // --- exe 執行完成後要做的事 ---
                if (exitCode == 0) {
                    // 假設成功就寄 email
                    EmailUtil.sendEmail(toEmail,"Video Download Successfully !!", videoName + " downloaded successfully." ,smtpDetails);
                } else {
                    // 失敗處理
                    EmailUtil.sendEmail(toEmail,"Video Download failed !!", videoName + " downloaded failed." ,smtpDetails);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 立即回應前端，不用等 exe 完成
        return ResponseEntity.ok().build();
    }

}
