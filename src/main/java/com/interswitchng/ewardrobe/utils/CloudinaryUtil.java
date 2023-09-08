package com.interswitchng.ewardrobe.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CloudinaryUtil {

    public String generatedFileName(String originalFileName) {
        String uniqueFileName = generateRandomFileName(originalFileName);
        return uniqueFileName;
    }

    private String generateRandomFileName(String originalFileName) {
        int randomSuffix = new Random().nextInt(1000) + 1;
        String extension = getFileExtension(originalFileName);
        return randomSuffix + "_" + originalFileName.replace("." + extension, "") + extension;
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }
}
