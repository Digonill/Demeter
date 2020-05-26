package com.server.demeter.config;

import java.security.cert.PKIXRevocationChecker.Option;
import java.util.Optional;

import com.server.demeter.domain.User;
import com.server.demeter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    UserRepository userRepo;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){

        User joao = new User("João", "Souza", "joao@gmail.com");
        User maria = new User("Maria", "Teixeira", "maria@gmail.com");
        User Laura = new User("Laura", "cordeiro", "Laura.cordeiro@gmail.com");

        createUserIfNotFound(joao);
        createUserIfNotFound(maria);
        createUserIfNotFound(Laura);
    }

    private User createUserIfNotFound(final User user)
    {
     Optional<User> obj = userRepo.findByEmail(user.getemail());

     if (obj.isPresent()){
         return obj.get();
     }
    return userRepo.save(user);

    }
}