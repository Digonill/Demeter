package com.server.demeter.resources;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.server.demeter.domain.Role;
import com.server.demeter.domain.User;
import com.server.demeter.dto.UserDTO;
import com.server.demeter.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserResource {

    @Autowired
    UserService service;

    TokenStore tokenStore = new InMemoryTokenStore();

    @Autowired
    DefaultTokenServices tokenServices = new DefaultTokenServices();
    

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

    @RequestMapping(value = "/users/main", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUserMain(Principal principal) {
        User user = this.service.findByEmail(principal.getName());

        UserDTO dto = new UserDTO(user);
        dto.setPassword("");
        return ResponseEntity.ok().body(dto);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {

            String tokenValue = authHeader.replace("Bearer", "").trim();
            
            OAuth2AccessToken accessToken = tokenServices.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
            tokenServices.revokeToken(String.valueOf(accessToken));
        
        }
        return ResponseEntity.noContent().build();
    }
}