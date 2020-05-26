package com.server.demeter.repository;

import java.util.Optional;

import com.server.demeter.domain.Role;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);    
}