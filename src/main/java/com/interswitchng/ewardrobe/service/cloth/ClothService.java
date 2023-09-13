package com.interswitchng.ewardrobe.service.cloth;

import com.interswitchng.ewardrobe.data.model.Cloth;
import com.interswitchng.ewardrobe.dto.GetAllClothesDto;
import com.interswitchng.ewardrobe.exception.EWardRobeException;
import com.interswitchng.ewardrobe.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ClothService {

    public void saveCloth(Cloth cloth);

    Optional<Cloth> findByPictureUrl(String pictureUrl);

    List<Cloth> findClothByUserId(String userId);

    Cloth findClothByUserIdAndImageUrl(String userId, String pictureUrl);

    String uploadImage(MultipartFile file, String category, String clothType)
            throws IOException, UserNotFoundException;

    Page<Cloth> getAllClothes(GetAllClothesDto getAllClothesDto, String userId, Pageable pageable) throws UserNotFoundException;

    void deleteCloth(String clothId) throws UserNotFoundException, EWardRobeException;
    List<Cloth> getAllUserClothes(String userId);
    List <Cloth> getAllUploadedUserClothes(String userId);
    List <Cloth> getAllUnsplashUserClothes(String userId);
}
