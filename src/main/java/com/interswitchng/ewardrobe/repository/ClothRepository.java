package com.interswitchng.ewardrobe.repository;

import com.interswitchng.ewardrobe.data.model.Category;
import com.interswitchng.ewardrobe.data.model.Cloth;
import com.interswitchng.ewardrobe.data.model.ClothType;
import com.interswitchng.ewardrobe.data.model.CollectionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ClothRepository extends MongoRepository<Cloth,String> {
    Optional<Cloth> findByImageUrl(String pictureUrl);
    List<Cloth> findClothByUserId(String userId);
    Cloth findClothByUserIdAndImageUrl(String userId, String pictureUrl);
    List<Cloth> findClothsByUserIdAndCategory(String id, Category category);

}
