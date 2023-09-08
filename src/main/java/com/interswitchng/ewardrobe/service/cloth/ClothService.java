package com.interswitchng.ewardrobe.service.cloth;

import com.interswitchng.ewardrobe.data.model.Cloth;

import java.util.Optional;

public interface ClothService {

    public void saveCloth(Cloth cloth);

    Optional<Cloth> findByPictureUrl(String pictureUrl);
}
