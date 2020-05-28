package com.server.demeter.services;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.server.demeter.domain.User;
import com.server.demeter.domain.VerificationToken;
import com.server.demeter.dto.UserDTO;
import com.server.demeter.repository.RoleRepository;
import com.server.demeter.repository.UserRepository;
import com.server.demeter.repository.VerificationTokenRepository;
import com.server.demeter.services.email.EmailService;
import com.server.demeter.services.exception.ObjectAlreadyExistException;
import com.server.demeter.services.exception.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailservice;

    public List<User> findall() {
        return userRepository.findAll();
    }

    public User findbyID(final String id) {
        final Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("Objeto não Encontrado"));
    }

    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(final User user) {
        final Optional<User> updateUser = userRepository.findById(user.getId());

        return updateUser
                .map(u -> userRepository.save(new User(u.getId(), user.getFirstName(), user.getLastName(),
                        user.getEmail(), u.getPassword(), u.isenabled())))
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
    }

    public User fromDTO(UserDTO userDTO) {
        return new User(userDTO);
    }

    public void delete(final String id) {
        userRepository.deleteById(id);
    }

    public User FromDTO(final UserDTO dto) {
        return new User(dto);
    }

    public User registerUser(User user) {
        if (emailExist(user.getEmail())) {
            throw new ObjectAlreadyExistException(String.format("Já extiste uma conta com esse endereço de email"));
        }
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER").get()));
        user.setEnabled(false);
        user = create(user);

        // Envia email de confirmação
        emailservice.sendConfirmationHtmlEmail(user, null, 0);
        return user;
    }

    private boolean emailExist(final String email) {
        final Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return true;
        }
        return false;
    }

    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken vToken = new VerificationToken(token, user);
        verificationTokenRepository.save(vToken);
    }

    public String validateVerificationToken(final String token) {

        final Optional<VerificationToken> vToken = verificationTokenRepository.findByToken(token);
        if (!vToken.isPresent()) {
            return "invalidToken";
        }

        final User user = vToken.get().getUser();
        final Calendar cal = Calendar.getInstance();

        if ((vToken.get().getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "expired";
        }

        user.setEnabled(true);
        this.userRepository.save(user);
        return null;
    }

    public User findByEmail(final String email) {
        final Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(() -> new ObjectNotFoundException(String.format("Usuário não encontrado!")));
    }

    public VerificationToken generateNewVerificationToken(String email, int select) {

        User user = findByEmail(email);
        Optional<VerificationToken> vToken = verificationTokenRepository.findByUser(user);
        VerificationToken newToken;

        if (vToken.isPresent()) {
            vToken.get().UpdateToken(UUID.randomUUID().toString());
            newToken = vToken.get();
        } else {
            newToken = new VerificationToken(UUID.randomUUID().toString(), user);
        }

        VerificationToken updatetoken = verificationTokenRepository.save(newToken);
        emailservice.sendConfirmationHtmlEmail(user, updatetoken, select);

        return updatetoken;
    }

    public String validatePasswordResetToken(final String idUser, final String token) {
        final Optional<VerificationToken> vToken = verificationTokenRepository.findByToken(token);

        if (vToken.isPresent() && !(idUser.equals(vToken.get().getUser().getId()))) {
            return "invalid Token";
        }

        final Calendar cal = Calendar.getInstance();
        if ((vToken.get().getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "Expired";
        }
        return null;
    }

	public VerificationToken getVerificationBytoken(String token) {
		return verificationTokenRepository.findByToken(token).orElseThrow(()-> new ObjectNotFoundException("Token não existe"));
	}

	public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
	}
}