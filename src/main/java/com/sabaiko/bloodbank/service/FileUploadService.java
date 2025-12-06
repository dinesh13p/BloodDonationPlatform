package com.sabaiko.bloodbank.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {
    
    private static final String UPLOAD_DIR = "uploads/";
    
    public String uploadFile(MultipartFile file, String subfolder) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR + subfolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
            : "";
        String filename = UUID.randomUUID().toString() + extension;
        
        // Save file
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Return relative path
        return subfolder + "/" + filename;
    }
    
    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(UPLOAD_DIR + filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
}


