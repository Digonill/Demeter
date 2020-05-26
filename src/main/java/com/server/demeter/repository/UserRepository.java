package com.server.demeter.repository;

import java.util.Optional;

import com.server.demeter.domain.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
    
    Optional<User> findByEmail(String Email);
}