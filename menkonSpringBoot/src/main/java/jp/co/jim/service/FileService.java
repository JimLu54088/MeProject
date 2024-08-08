package jp.co.jim.service;

import jp.co.jim.entity.FileMetaData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileService {
    private final Path storageDir;
    private final Map<String, FileMetaData> fileStore = new HashMap<>();

    public FileService(@Value("${file.upload-dir}") String uploadDir) throws IOException {
        this.storageDir = Paths.get(uploadDir);
        Files.createDirectories(storageDir);
    }

    public String saveFile(MultipartFile file) throws IOException {
        String uniqueID = UUID.randomUUID().toString();
        Path destinationFile = storageDir.resolve(Paths.get(uniqueID)).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile);

        FileMetaData metadata = new FileMetaData(uniqueID, file.getOriginalFilename(), LocalDateTime.now().plusHours(1));
        fileStore.put(uniqueID, metadata);

        return uniqueID;
    }

    public FileMetaData getFileMetadata(String fileId) {
        return fileStore.get(fileId);
    }

    public Resource loadFile(String fileId) {
        FileMetaData metadata = fileStore.get(fileId);
        if (metadata != null && metadata.getExpiryTime().isAfter(LocalDateTime.now())) {
            Path filePath = storageDir.resolve(fileId).normalize().toAbsolutePath();
            return new FileSystemResource(filePath);
        }
        return null;
    }
}

