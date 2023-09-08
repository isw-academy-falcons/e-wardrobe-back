package com.interswitchng.ewardrobe.service.cloth;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.interswitchng.ewardrobe.data.model.*;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import com.interswitchng.ewardrobe.repository.ClothRepository;
import com.interswitchng.ewardrobe.repository.UserRepository;
import com.interswitchng.ewardrobe.utils.CloudinaryUtil;
import com.interswitchng.ewardrobe.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClothServiceImpl implements ClothService{
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;
    private final UserUtil userUtil;
    private final CloudinaryUtil cloudinaryUtil;
    private final ClothRepository clothRepository;

    @Override
    public String uploadImage(MultipartFile file, String category, String description, String clothType, String collectionType)
            throws IOException, UserNotFoundException {
        String email = userUtil.getAuthenticatedUserEmail();
        User user = userRepository.findUserByEmailIgnoreCase(email).orElseThrow(() -> new UserNotFoundException("User cannot be found"));

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
        cloth.setCollectionType(CollectionType.valueOf(collectionType));
        cloth.setUserId(user.getUserId());
        cloth.setUrl(imageUrl);

        clothRepository.save(cloth);
        return imageUrl;
    }
}
