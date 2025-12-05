package com.ajemi.backend.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
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
        } catch (Exception e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            String ext = "";
            if (file.getOriginalFilename().contains(".")) {
                ext = file.getOriginalFilename()
                        .substring(file.getOriginalFilename().lastIndexOf("."));
            }

            String filename = UUID.randomUUID() + ext;

            Files.copy(
                file.getInputStream(),
                this.root.resolve(filename),
                StandardCopyOption.REPLACE_EXISTING
            );

            return "/uploads/" + filename;

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file!");
        }
    }
}
