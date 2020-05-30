package com.server.demeter.resources;

import com.server.demeter.domain.User;
import com.server.demeter.domain.VerificationToken;
import com.server.demeter.dto.UserDTO;
import com.server.demeter.services.UserService;
import com.server.demeter.util.GenericResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class RegistrationResource {

    @Autowired
    UserService userService;

    @PostMapping("/registration/users")
    public ResponseEntity<Void> registerUser(@RequestBody UserDTO userDTO) {

        User user = this.userService.fromDTO(userDTO);
        this.userService.registerUser(user);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/regitrationConfirm/users")
    public ResponseEntity<GenericResponse> confirmRegistrationUser(@RequestParam("token") String token) {

        final Object result = this.userService.validateVerificationToken(token);

        if (result == null) {
            return ResponseEntity.ok().body(new GenericResponse("Success"));
        }

        return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result.toString()));
    }

    @GetMapping("/resendRegistrationToken/users")
    public ResponseEntity<Void> resendRegistrationToken(@RequestParam("email") String email) {
        this.userService.generateNewVerificationToken(email, 0);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<Void> resetPassword(@RequestParam("email") final String email) {
        this.userService.generateNewVerificationToken(email, 1);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/changepassword/users")
    public ResponseEntity<GenericResponse> changePassword(@RequestParam("id") String idUser,
            @RequestParam("token") String token) {
        String result = userService.validatePasswordResetToken(idUser, token);

        if (result == null) {
            return ResponseEntity.ok().body(new GenericResponse("success"));
        }

        return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result));
    }

    @PostMapping("/savepassword/users")
    public ResponseEntity<GenericResponse> savePassword(@RequestParam("token") String token, @RequestParam("password") String password) {
        String result = userService.validateVerificationToken(token);

        if (result != null) {
            return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result)); 
        }
        final VerificationToken vToken = userService.getVerificationBytoken(token);
        if (vToken !=null){
            userService.changeUserPassword(vToken.getUser(), password);
        }

        return ResponseEntity.noContent().build();

    }
}