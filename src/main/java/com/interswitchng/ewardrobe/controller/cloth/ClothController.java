package com.interswitchng.ewardrobe.controller.cloth;

import com.interswitchng.ewardrobe.data.model.Cloth;
import com.interswitchng.ewardrobe.dto.GetAllClothesDto;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.service.cloth.ClothService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/all/clothes")
    public Page<Cloth> getClothes(
            @RequestParam(name = "clothType", required = false) String clothType,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "collectionType", required = false) String collectionType,
            @RequestParam String userId,
            @RequestParam(defaultValue = "10") int page,
            @RequestParam(defaultValue = "10") int size) throws UserNotFoundException {

        Pageable pageable = PageRequest.of(page, size);

        GetAllClothesDto getAllClothesDto = new GetAllClothesDto();
        getAllClothesDto.setClothType(clothType);
        getAllClothesDto.setCategory(category);
        getAllClothesDto.setCollectionType(collectionType);


        return clothService.getAllClothes(getAllClothesDto, userId,pageable);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCloth(@RequestParam String clothId, @RequestParam String userId) {
        try {

            clothService.deleteCloth(clothId, userId);
            return ResponseEntity.ok("Cloth deleted successfully");
        } catch (EWardRobeException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cloth not found");
        }
    }
}
