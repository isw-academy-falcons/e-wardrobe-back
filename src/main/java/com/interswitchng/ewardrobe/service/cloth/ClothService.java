package com.interswitchng.ewardrobe.service.cloth;

import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ClothService {
    String uploadImage(MultipartFile file, String category, String description, String clothType, String collectionType)
            throws IOException, UserNotFoundException;

}
