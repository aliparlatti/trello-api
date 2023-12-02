package com.trello.trello.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.trello.trello.security.GoogleTokenVerifier;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Path;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/uploads")
public class FileController {
    private final GoogleTokenVerifier tokenService;

    public FileController(GoogleTokenVerifier tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String uploadDir = "uploads";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdir();
        }
        try {
            Path filePath = Path.of(uploadDir, file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return (ResponseEntity<String>) ResponseEntity.status(200).body("");
        } catch (IOException e) {
            return (ResponseEntity<String>) ResponseEntity.status(500)
                    .body("");
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName, @RequestParam("token") String token) {
        if (!tokenService.isValidToken(token)) {
            return ResponseEntity.status(401).build();
        }

        Path filePath = Path.of("uploads", fileName);
        Resource resource = new FileSystemResource(filePath);

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
