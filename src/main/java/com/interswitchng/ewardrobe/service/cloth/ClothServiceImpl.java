package com.interswitchng.ewardrobe.service.cloth;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.interswitchng.ewardrobe.data.model.*;
import com.interswitchng.ewardrobe.dto.GetAllClothesDto;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.repository.ClothRepository;
import com.interswitchng.ewardrobe.service.user.UserService;
import com.interswitchng.ewardrobe.utils.CloudinaryUtil;
import com.interswitchng.ewardrobe.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClothServiceImpl implements ClothService {

    private final UserService userService;
    private final Cloudinary cloudinary;
    private final UserUtil userUtil;
    private final CloudinaryUtil cloudinaryUtil;
    private final ClothRepository clothRepository;

    @Override
    public void saveCloth(Cloth cloth) {
        clothRepository.save(cloth);
    }

    @Override
    public Optional<Cloth> findByPictureUrl(String pictureUrl) {
        return clothRepository.findByImageUrl(pictureUrl);
    }

    @Override
    public List<Cloth> findClothByUserId(String userId) {
        return clothRepository.findClothByUserId(userId);
    }

    @Override
    public Cloth findClothByUserIdAndImageUrl(String userId, String pictureUrl) {
        return clothRepository.findClothByUserIdAndImageUrl(userId, pictureUrl);
    }

    @Override
    public String uploadImage(MultipartFile file, String category, String description, String clothType, String collectionType)
            throws IOException, UserNotFoundException {
        String email = userUtil.getAuthenticatedUserEmail();
        User user = userService.findUserByEmail(email);

        byte[] fileBytes = file.getBytes();
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = cloudinaryUtil.generatedFileName(originalFileName);
        Map<String, String> uploadOptions = ObjectUtils.asMap("public_id", uniqueFileName);

        Map<String, String> uploadResult = cloudinary.uploader().upload(fileBytes, uploadOptions);
        String imageUrl = uploadResult.get("secure_url");

        Cloth cloth = new Cloth();
        cloth.setClothType(ClothType.valueOf(clothType));
        cloth.setDescription(description);
        cloth.setCategory(Category.valueOf(category));
        cloth.setCollectionType(CollectionType.UPLOADED);
        cloth.setUserId(user.getUserId());
        cloth.setImageUrl(imageUrl);

        clothRepository.save(cloth);
        return imageUrl;
    }

    @Override
    public Page<Cloth> getAllClothes(GetAllClothesDto getAllClothesDto, String userId, Pageable pageable) throws UserNotFoundException {
        User foundUser = userService.findById(userId);
        List<Cloth> clothByUserId = clothRepository.findClothByUserId(foundUser.getUserId());
        List<Cloth> filteredClothes = new ArrayList<>();

        for (Cloth cloth : clothByUserId) {
            boolean includeCloth = true;

            if (getAllClothesDto.getCollectionType() != null && !getAllClothesDto.getCollectionType().isEmpty() &&
                    !getAllClothesDto.getCollectionType().equals(cloth.getCollectionType().name())) {
                includeCloth = false;
            }

            if (getAllClothesDto.getClothType() != null && !getAllClothesDto.getClothType().isEmpty() &&
                    !getAllClothesDto.getClothType().equals(cloth.getClothType().name())) {
                includeCloth = false;
            }

            if (getAllClothesDto.getCategory() != null && !getAllClothesDto.getCategory().isEmpty() &&
                    !getAllClothesDto.getCategory().equals(cloth.getCategory().name())) {
                includeCloth = false;
            }

            if (includeCloth) {
                filteredClothes.add(cloth);
            }
            else{
                filteredClothes.add(cloth);
            }
        }
        

        return new PageImpl<>(filteredClothes, pageable, filteredClothes.size());
    }


    @Override
    public void deleteCloth(String clothId, String userId) throws UserNotFoundException, EWardRobeException {

        User foundUser = userService.findById(userId);

        Cloth cloth = clothRepository.findById(clothId).orElseThrow(() ->
                new EWardRobeException("Cloth not found"));


        if (!cloth.getUserId().equals(foundUser.getUserId())) {
            throw new UserNotFoundException("User is not authorized to delete this cloth");
        }

        clothRepository.delete(cloth);
    }

}
