package com.interswitchng.ewardrobe.service.cloth;

import com.interswitchng.ewardrobe.data.model.Cloth;

import java.util.List;
import java.util.Optional;

public interface ClothService {

    public void saveCloth(Cloth cloth);

    Optional<Cloth> findByPictureUrl(String pictureUrl);

    List<Cloth> findClothByUserId(String userId);

    List<Cloth> findClothByUserIdAndImageUrl(String userId, String pictureUrl);
}
