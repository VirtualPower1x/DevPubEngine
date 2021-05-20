package com.skillbox.devpubengine.service.general;

import com.skillbox.devpubengine.exception.FileUploadException;
import com.skillbox.devpubengine.utils.ImageResizer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Random;

@Service
public class PhotoService {

    public String uploadPhoto (MultipartFile photo) {
        String subdirs = buildDirectories();
        String filePath = subdirs + "/" + randomHash(6) + ".png";
        try {
            BufferedImage resizedPhoto = ImageResizer.resizeImage(photo);
            if (!Files.isDirectory(Path.of(
                    System.getProperty("user.dir") + "/src/main/resources/static" + subdirs))){
                Files.createDirectories(Path.of(
                        System.getProperty("user.dir") + "/src/main/resources/static" + subdirs));
            }
            File newFile = new File(System.getProperty("user.dir") + "/src/main/resources/static" + filePath);
            ImageIO.write(Objects.requireNonNull(resizedPhoto), "png", newFile);
        }
        catch (IOException e) {
            throw new FileUploadException();
        }
        return filePath;
    }

    private String buildDirectories() {
        return "/user-photos/" + randomHash(2) +
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
