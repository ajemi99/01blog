package com.ajemi.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads").toAbsolutePath().normalize();

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
            if (originalFilename == null) return null;

            // 🚩 1. Extract & Validate Extension
            String ext = "";
            int dotIndex = originalFilename.lastIndexOf(".");
            if (dotIndex >= 0) {
                ext = originalFilename.substring(dotIndex).toLowerCase();
            }

            // 🛑 Security Check: Ghabal l-extensions li m-smou7 bihom
            if (!isSupportedExtension(ext)) {
                throw new RuntimeException("File extension not supported!");
            }

            // 🚩 2. Generate Unique Filename
            String filename = UUID.randomUUID().toString() + ext;
            Path destinationFile = this.root.resolve(filename).normalize();

            // 🚩 3. Save File
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Could not store the file!", e);
        }
    }

    private boolean isSupportedExtension(String ext) {
        // Zid ga3 l-formats li bghiti f l-app dyalk
        return List.of(".jpg", ".jpeg", ".png", ".gif", ".mp4", ".mov").contains(ext);
    }

    public void deleteFile(String mediaUrl) {
        if (mediaUrl == null || mediaUrl.isBlank()) return;

        try {
            // mediaUrl = /uploads/xxxx.jpg
            String filename = mediaUrl.replace("/uploads/", "");
            
            // 🛑 Normalize path bach t-fadi path traversal security issues
            Path filePath = root.resolve(filename).normalize();

            // Check wach l-file baqi wast l-folder dyal uploads machi kharjou
            if (filePath.startsWith(root)) {
                Files.deleteIfExists(filePath);
            }

        } catch (IOException e) {
            System.err.println("Could not delete file: " + mediaUrl);
        }
    }
}