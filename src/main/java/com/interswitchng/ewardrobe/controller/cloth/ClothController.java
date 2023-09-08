package com.interswitchng.ewardrobe.controller.cloth;

import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.service.cloth.ClothService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cloth")
public class ClothController {

    private final ClothService clothService;
    @PostMapping("/upload")
    public ResponseEntity<String> uploadOutfit(@RequestParam("file") MultipartFile file,
                                               @RequestParam("category") String category,
                                               @RequestParam("description") String description,
                                               @RequestParam("clothType") String clothType,
                                               @RequestParam("collectionType") String collectionType)
            throws IOException, UserNotFoundException {
        return new ResponseEntity<>(
                clothService.uploadImage(file, category, description, clothType, collectionType), HttpStatus.OK);
    }
}
