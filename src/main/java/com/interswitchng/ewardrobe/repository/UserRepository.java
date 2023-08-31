package com.interswitchng.ewardrobe.repository;

import com.interswitchng.ewardrobe.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
