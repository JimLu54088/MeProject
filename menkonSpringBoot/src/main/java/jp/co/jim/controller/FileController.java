package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.entity.FileMetaData;
import jp.co.jim.entity.FileUploadResponseEntity;
import jp.co.jim.service.FileService;
import jp.co.jim.service.URLShortService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger logger = LogManager.getLogger(FileController.class);
    private static final String LOG_HEADER = "[" + FileController.class.getSimpleName() + "] :: ";

    private static final String ERROR_LOG_HEADER = "[" + FileController.class.getName() + "] :: ";

    @Autowired
    private FileService fileService;

    @Autowired
    private URLShortService uRLShortService;

    @Autowired
    private Environment environment;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {

            FileUploadResponseEntity entity = new FileUploadResponseEntity();

            String fileId = fileService.saveFile(file);
            String downloadUrl = "/files/download/" + fileId;
            String shortendownloadUrl = uRLShortService.shortenURL(downloadUrl);

            entity.setDownloadUrl(downloadUrl);
            entity.setShortendownloadUrl(shortendownloadUrl);

            Gson gson = new Gson();

            logger.debug(LOG_HEADER + "Json of entity : " + gson.toJson(entity));

            // 獲取本機 IP 地址
            InetAddress inetAddress = InetAddress.getLocalHost();
            // 輸出 IPv4 地址
            logger.debug(LOG_HEADER + "IPv4 Address: " + inetAddress.getHostAddress());

            String serverPort = environment.getProperty("local.server.port");


            logger.debug(LOG_HEADER + "DownloadLink : http://" + inetAddress.getHostAddress() + ":" + serverPort + "/files/download/" + fileId);
            return ResponseEntity.ok(gson.toJson(entity));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        Resource file = fileService.loadFile(fileId);
        FileMetaData metadata = fileService.getFileMetadata(fileId);

        if (file == null || metadata == null) {
            return ResponseEntity.notFound().build();
        }

        String contentDisposition = "attachment; filename=\"" + metadata.getOriginalFilename() + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(file);
    }
}

