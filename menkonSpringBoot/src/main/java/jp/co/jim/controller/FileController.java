package jp.co.jim.controller;

import jp.co.jim.entity.FileMetaData;
import jp.co.jim.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = fileService.saveFile(file);
            String downloadUrl = "/files/download/" + fileId;
            return ResponseEntity.ok(downloadUrl);
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

