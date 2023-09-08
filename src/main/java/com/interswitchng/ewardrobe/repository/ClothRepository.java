package com.interswitchng.ewardrobe.repository;

import com.interswitchng.ewardrobe.data.model.Cloth;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ClothRepository extends MongoRepository<Cloth,String> {
    Optional<Cloth> findByImageUrl(String pictureUrl);
    List<Cloth> findClothByUserId(String userId);

    Cloth findClothByUserIdAndImageUrl(String userId, String pictureUrl);
}
