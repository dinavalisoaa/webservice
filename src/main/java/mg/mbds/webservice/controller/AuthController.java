package mg.mbds.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import mg.mbds.webservice.dto.LoginDTO;
import mg.mbds.webservice.dto.RegisterDTO;
import mg.mbds.webservice.service.AuthService;
import mg.mbds.webservice.model.User;
import mg.mbds.webservice.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        String token = authService.login(loginDto.getEmail(), loginDto.getPassword());
        if (token != null) {
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        if (registerDTO.getRole() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role is required");
        }
        if (registerDTO.getPassword() == null || !registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setRole(registerDTO.getRole());
        user.setPasswordHash(passwordEncoder.encode(registerDTO.getPassword()));

        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}