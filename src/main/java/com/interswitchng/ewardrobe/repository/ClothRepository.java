package com.interswitchng.ewardrobe.repository;

import com.interswitchng.ewardrobe.data.model.Cloth;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClothRepository extends MongoRepository<Cloth,String> {
}
