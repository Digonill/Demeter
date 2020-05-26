package com.server.demeter.services;

import java.util.List;
import java.util.Optional;

import javax.jws.soap.SOAPBinding.Use;

import com.server.demeter.domain.User;
import com.server.demeter.dto.UserDTO;
import com.server.demeter.repository.UserRepository;
import com.server.demeter.services.exception.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findall() {
        return userRepository.findAll();
    }

    public User findbyID(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("Objeto não Encontrado"));
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(User user){
        Optional<User> updateUser = userRepository.findById(user.getId());

        return updateUser.map(u -> userRepository.save(new User(u.getId(),user.getFirstName(),user.getLastName(),user.getemail())))
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
    }

    public void delete(String id){
        userRepository.deleteById(id);
    }

    public User FromDTO(UserDTO dto) {
        return new User(dto);
    }
}