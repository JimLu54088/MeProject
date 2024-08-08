package jp.co.jim.entity;

import java.time.LocalDateTime;

public class FileMetaData {
    private String id;
    private String originalFilename;
    private LocalDateTime expiryTime;

    public FileMetaData(String id, String originalFilename, LocalDateTime expiryTime) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.expiryTime = expiryTime;
    }

    public String getId() {
        return id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }
}

