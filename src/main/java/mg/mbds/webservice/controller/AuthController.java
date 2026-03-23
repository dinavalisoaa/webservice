package mg.mbds.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import mg.mbds.webservice.dto.LoginDTO;
import mg.mbds.webservice.service.AuthService;
import mg.mbds.webservice.model.User;
import mg.mbds.webservice.model.Role;
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
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        if(user.getRole() == null) {
            user.setRole(Role.NURSE);
        }
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}
