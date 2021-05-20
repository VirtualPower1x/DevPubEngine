package com.skillbox.devpubengine.service.general;

import com.skillbox.devpubengine.exception.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Random;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class ImageService {

    public String uploadImage(MultipartFile image) {
        final long MAX_FILE_SIZE = 10 * 1024 * 1024;
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new FileUploadException("image");
        }

        String extension = getExtension(image.getOriginalFilename()).orElse("");
        if (!extension.matches("\\.jpe?g\\b|\\.png\\b")) {
            throw new FileUploadException("extension");
        }
        String subdirs = buildDirectories();
        String filePath = subdirs + "/" + randomHash(6) + extension;

        try {
            if (!Files.isDirectory(Path.of(System.getProperty("user.dir") + subdirs))){
                Files.createDirectories(Path.of(System.getProperty("user.dir") + subdirs));
            }
            Files.copy(
                    image.getInputStream(),
                    Path.of(System.getProperty("user.dir") + filePath),
                    REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new FileUploadException();
        }
        return filePath;
    }

    private Optional<String> getExtension (String filename) {
        return Optional
                .ofNullable(filename)
                .filter(s -> s.contains("."))
                .map(s -> s.substring(s.lastIndexOf(".")));
    }

    private String buildDirectories() {
        return "/upload/" + randomHash(2) +
                "/" +
                randomHash(2) +
                "/" +
                randomHash(2);
    }

    private String randomHash(int length) {
        Random random = new Random();
        return Long.toHexString(random.nextLong()).substring(0, length);
    }
}
