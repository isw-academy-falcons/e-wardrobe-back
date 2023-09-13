package com.interswitchng.ewardrobe.repository;

import com.interswitchng.ewardrobe.data.model.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {

    Optional<VerificationToken> findByToken(String token);
     List<VerificationToken> findByExpirationDateBeforeAndTimeUsedNull(LocalDateTime expirationTime);
}
