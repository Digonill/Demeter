package com.server.demeter.config;

import java.util.Arrays;
import java.util.Optional;

import com.server.demeter.domain.Role;
import com.server.demeter.domain.User;
import com.server.demeter.repository.RoleRepository;
import com.server.demeter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){

        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role roleAdmin = createRoleIfNotFound("ROLE_ADMIN");
        Role roleUser = createRoleIfNotFound("ROLE_USER");

        User joao = new User("João", "Souza", "joao@gmail.com");
        joao.setRole(Arrays.asList(roleAdmin));
        
        User maria = new User("Maria", "Teixeira", "maria@gmail.com");
        maria.setRole(Arrays.asList(roleUser));
        
        User Laura = new User("Laura", "cordeiro", "Laura.cordeiro@gmail.com");

        createUserIfNotFound(joao);
        createUserIfNotFound(maria);
        createUserIfNotFound(Laura);
    }

    private User createUserIfNotFound(final User user)
    {
     Optional<User> obj = userRepository.findByEmail(user.getemail());

     if (obj.isPresent()){
         return obj.get();
     }
    return userRepository.save(user);

    }

    private Role createRoleIfNotFound(String name){
        Optional<Role> role = roleRepository.findByName(name);
        if (role.isPresent()){
            return role.get();
        }
        return roleRepository.save(new Role(name));
    }
}