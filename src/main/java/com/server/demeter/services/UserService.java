package com.server.demeter.services;

import java.util.List;

import com.server.demeter.domain.User;
import com.server.demeter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findall(){
        return userRepository.findAll();
    }

}