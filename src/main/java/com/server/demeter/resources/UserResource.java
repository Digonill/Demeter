package com.server.demeter.resources;

import java.util.List;

import com.server.demeter.domain.User;
import com.server.demeter.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") 
public class UserResource {

    @Autowired
    UserService service;
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll(){

        List<User> users =  service.findall();
        return ResponseEntity.ok().body(users);
    }
}