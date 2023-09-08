package com.interswitchng.ewardrobe.service.cloth;

import com.interswitchng.ewardrobe.data.model.Cloth;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ClothService {

    public void saveCloth(Cloth cloth);

    Optional<Cloth> findByPictureUrl(String pictureUrl);

    List<Cloth> findClothByUserId(String userId);

    Cloth findClothByUserIdAndImageUrl(String userId, String pictureUrl);

    String uploadImage(MultipartFile file, String category, String description, String clothType, String collectionType)
            throws IOException, UserNotFoundException;

}
