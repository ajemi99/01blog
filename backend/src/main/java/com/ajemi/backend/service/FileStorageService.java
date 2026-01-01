package com.ajemi.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!", e);
        }
    }

    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            String originalFilename = file.getOriginalFilename();
            String ext = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String filename = UUID.randomUUID() + ext;

            Files.copy(
                file.getInputStream(),
                this.root.resolve(filename),
                StandardCopyOption.REPLACE_EXISTING
            );

            return "/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Could not store the file!", e);
        }
    }
    public void deleteFile(String mediaUrl) {
    if (mediaUrl == null || mediaUrl.isBlank()) return;

    try {
        // mediaUrl = /uploads/xxxx.jpg
        String filename = mediaUrl.replace("/uploads/", "");

        Path filePath = root.resolve(filename).normalize();
        Files.deleteIfExists(filePath);

    } catch (IOException e) {
        System.err.println("Could not delete file: " + mediaUrl);
    }
    }

}
