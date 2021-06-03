package com.skillbox.devpubengine.service.general;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.skillbox.devpubengine.exception.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

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
        String filePath = subdirs + "/" + randomHash(6);
        String target;
        try {
            Cloudinary cloudinary = Singleton.getCloudinary();
            target = cloudinary.uploader().upload(image.getBytes(), Map.of(
                    "public_id", filePath,
                    "overwrite", true,
                    "use_filename", true,
                    "unique_filename", true))
                    .get("secure_url").toString();
        }
        catch (IOException e) {
            throw new FileUploadException();
        }
        return target;
    }

    private Optional<String> getExtension (String filename) {
        return Optional
                .ofNullable(filename)
                .filter(s -> s.contains("."))
                .map(s -> s.substring(s.lastIndexOf(".")));
    }

    private String buildDirectories() {
        return "upload/" + randomHash(2) +
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
