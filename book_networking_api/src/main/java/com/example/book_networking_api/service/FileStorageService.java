package com.example.book_networking_api.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    @Value("./uploads")
    private String fileUploadPath;

    public String saveFile(@NotNull MultipartFile sourceFile, @NotNull Integer userId) {
        final String fileUploadSubPath = "users" + separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(@NotNull MultipartFile sourceFile, @NotNull String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if(!targetFolder.exists()){
            boolean folderCreated = targetFolder.mkdirs();
            if(!folderCreated){
                log.warn("Failed to create the target folder");
                return null;
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = fileUploadPath + separator + System.currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try{
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File saved to "+ targetFilePath);
        } catch (IOException e){
            log.error("File was not saved", e);
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        if(originalFilename == null || originalFilename.isEmpty()){
            return "";
        }

        int lastDotIndex = originalFilename.lastIndexOf(".");
        if(lastDotIndex == -1){
            return "";
        }
        return fileUploadPath.substring(lastDotIndex + 1).toLowerCase();
    }
}
