package com.skillbox.devpubengine.utils;

import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageResizer {

    private static final int IMG_WIDTH = 36;
    private static final int IMG_HEIGHT = 36;

    public static BufferedImage resizeImage (MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            return null;
        }
        if (image.getWidth() != image.getHeight()) {
            image = Scalr.crop(
                    image,
                    ((image.getWidth() - Math.min(image.getWidth(), image.getHeight())) / 2),
                    ((image.getHeight() - Math.min(image.getWidth(), image.getHeight())) / 2),
                    Math.min(image.getWidth(), image.getHeight()),
                    Math.min(image.getWidth(), image.getHeight()),
                    Scalr.OP_ANTIALIAS);
        }
        return Scalr.resize(image, IMG_WIDTH, IMG_HEIGHT);
    }
}