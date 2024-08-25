package jp.co.jim.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/fenXiangApi")
public class FileFenXianController {

    // The directory where the files are located
    @Value("${FILE_FENXIANG_DIRECTORY}")
    private String FILE_FENXIANG_DIRECTORY;

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            // Construct the file path
            Path filePath = Paths.get(FILE_FENXIANG_DIRECTORY).resolve(filename).normalize();

            // Check if file exists
            File file = filePath.toFile();
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Load file as resource
            Resource resource = new UrlResource(filePath.toUri());

            // Return the resource as a downloadable file
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
