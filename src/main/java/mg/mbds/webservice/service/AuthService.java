package mg.mbds.webservice.service;

import mg.mbds.webservice.dto.RegisterDTO;
import mg.mbds.webservice.exception.UnauthorizedException;
import mg.mbds.webservice.model.User;
import mg.mbds.webservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPasswordHash()))
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        return jwtService.generateToken(user.getEmail(), user.getRole().name());
    }

    public User register(RegisterDTO dto) {
        validateRegistration(dto);
        User user = buildUser(dto);
        return userRepository.save(user);
    }

    private void validateRegistration(RegisterDTO dto) {
        if (dto.getRole() == null) {
            throw new IllegalArgumentException("Role is required");
        }
        if (dto.getPassword() == null || !dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

    private User buildUser(RegisterDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        return user;
    }
}
