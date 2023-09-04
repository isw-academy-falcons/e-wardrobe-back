package com.interswitchng.ewardrobe.repository;

import com.interswitchng.ewardrobe.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findUserByEmailIgnoreCase(String email);

}
