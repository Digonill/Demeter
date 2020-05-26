package com.server.demeter.resources;

import java.util.List;
import java.util.stream.Collectors;

import com.server.demeter.domain.Role;
import com.server.demeter.domain.User;
import com.server.demeter.dto.UserDTO;
import com.server.demeter.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserResource {

    @Autowired
    UserService service;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> findAll() {

        final List<User> users = service.findall();
        final List<UserDTO> usersDTO = users.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());

        return ResponseEntity.ok().body(usersDTO);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> findbyID(@PathVariable final String id) {
        final User user = service.findbyID(id);

        return ResponseEntity.ok().body(new UserDTO(user));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> create(@RequestBody final UserDTO userdto) {
        final User user = service.FromDTO(userdto);

        return ResponseEntity.ok().body(new UserDTO(service.create(user)));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> create(@PathVariable final String id, @RequestBody final UserDTO userdto) {

        final User user = service.FromDTO(userdto);
        user.setId(id);

        return ResponseEntity.ok().body(new UserDTO(service.update(user)));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}/roles")
    public ResponseEntity<List<Role>> findRoles(@PathVariable String id) {
        User user = service.findbyID(id);
        return ResponseEntity.ok().body(user.getRoles());
    }
}