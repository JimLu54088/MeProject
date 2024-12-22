package jp.co.jim.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/subtitles")
public class SubtitleController {

    @Value("${subtitlePath}")
    private String subtitlePath;

    private Path subtitleDirectory;

    @PostConstruct
    public void init() {
        this.subtitleDirectory = Paths.get(subtitlePath);
    }

    @GetMapping
    public ResponseEntity<?> listSubtitles() throws IOException {
        return ResponseEntity.ok(
                Files.list(subtitleDirectory)
                        .filter(Files::isRegularFile)
                        .filter(file -> file.toString().endsWith(".vtt"))
                        .map(file -> file.getFileName().toString())
                        .collect(Collectors.toList())
        );
    }


    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getSubtitle(@PathVariable String filename) throws IOException {
        Path filePath = subtitleDirectory.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
