package com.server.demeter.repository;

import java.util.Optional;

import com.server.demeter.domain.User;
import com.server.demeter.domain.VerificationToken;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {

    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(User user);
    
}