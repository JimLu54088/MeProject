package jp.co.jim.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Value("${videoPath}")
    private String videoPath;

    private Path videoDirectory;

    @PostConstruct
    public void init() {
        this.videoDirectory = Paths.get(videoPath);
    }

    public Path getVideoDirectory() {
        return videoDirectory;
    }

    @GetMapping
    public ResponseEntity<?> listVideos() throws IOException {
        return ResponseEntity.ok(
                Files.list(videoDirectory)
                        .filter(Files::isRegularFile)
                        .filter(file -> file.toString().matches(".*\\.(mp4|mkv|avi|mov|wmv)$"))
                        .map(file -> file.getFileName().toString())
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/stream/{filename}")
    public ResponseEntity<Resource> streamVideo(@PathVariable String filename) throws IOException {
        Path filePath = videoDirectory.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }







}
