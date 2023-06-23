package com.example.code.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUitl {
    private String path = "src/main/resources/static/image";

    public String addFileToStorage(MultipartFile file) {
        try {
            String nameFile = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(path, nameFile);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return nameFile;
        } catch (Exception e) {
            return "";
        }
    }

    public String readFile(String fileString) {
        try {
            Path fileResource = Paths.get(path).resolve(fileString);
            Resource resource = new UrlResource(fileResource.toUri());
            byte[] imageBytes = StreamUtils.copyToByteArray(resource.getInputStream());
            return java.util.Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            return null;
        }
    }
}
