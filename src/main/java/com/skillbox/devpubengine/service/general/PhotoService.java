package com.skillbox.devpubengine.service.general;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.skillbox.devpubengine.exception.FileUploadException;
import com.skillbox.devpubengine.utils.ImageResizer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class PhotoService {

    public String uploadPhoto (MultipartFile photo) {
        String subdirs = buildDirectories();
        String filePath = subdirs + "/" + randomHash(6);
        String target;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            BufferedImage resizedPhoto = ImageResizer.resizeImage(photo);
            if (resizedPhoto == null) {
                throw new FileUploadException();
            }
            ImageIO.write(resizedPhoto, getExtension(photo.getOriginalFilename()).orElse(""), baos);
            Cloudinary cloudinary = Singleton.getCloudinary();
            target = cloudinary.uploader().upload(baos.toByteArray(), Map.of(
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
                .map(s -> s.substring(s.lastIndexOf(".") + 1));
    }

    private String buildDirectories() {
        return "user-photos/" + randomHash(2) +
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
