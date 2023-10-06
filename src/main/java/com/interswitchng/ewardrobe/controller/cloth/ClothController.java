package com.interswitchng.ewardrobe.controller.cloth;

import com.interswitchng.ewardrobe.data.model.Category;
import com.interswitchng.ewardrobe.data.model.Cloth;
import com.interswitchng.ewardrobe.dto.ClothListDto;
import com.interswitchng.ewardrobe.dto.GetAllClothesDto;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.service.cloth.ClothService;
import lombok.RequiredArgsConstructor;
import org.python.antlr.ast.Str;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cloth")
public class ClothController {

    private final ClothService clothService;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadOutfit(@RequestParam("file") List<MultipartFile> file,
                                               @RequestParam("category") String category,
                                               @RequestParam("clothType") String clothType)
            throws IOException, UserNotFoundException {
        List<String> response  = clothService.uploadImage(file, category, clothType);
        return new ResponseEntity<>(response, OK);
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


        return clothService.getAllClothes(getAllClothesDto, userId, pageable);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCloth(@RequestParam String clothId) {
        try {

            clothService.deleteCloth(clothId);
            return ResponseEntity.ok("Cloth deleted successfully");
        } catch (EWardRobeException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cloth not found");
        }
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Cloth>> userClothes(@PathVariable String userId) {
        List<Cloth> clothes = clothService.getAllUserClothes(userId);
        return new ResponseEntity<>(clothes, OK);
    }

    @GetMapping("/generate/{userId}")
    public ResponseEntity<?> generateOutfits(@PathVariable String userId, @RequestParam Category category) throws EWardRobeException {
        var response = clothService.generateOutfit(userId, category);
        return new ResponseEntity<>(response, OK);
    }

    @GetMapping("/generate_dress/{userId}")
    public ResponseEntity<?> generateDress(@PathVariable String userId, @RequestParam Category category) throws EWardRobeException {
        var response = clothService.generateDress(userId, category);
        return new ResponseEntity<>(response, OK);
    }
}
