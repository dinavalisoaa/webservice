package mg.mbds.webservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mg.mbds.webservice.model.User;
import mg.mbds.webservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService { 
    @Autowired 
    UserRepository repo; 

    @Autowired 
    JwtService jwtService; 

    @Autowired
    PasswordEncoder passwordEncoder;

    public String login(String email, String password){ 
        Optional<User> user = repo.findByEmail(email); 
        if(user.isEmpty() || !passwordEncoder.matches(password, user.get().getPasswordHash())){ 
            return null; 
        } 
        return jwtService.generateToken( 
            user.get().getEmail(),
            user.get().getRole().name()
        );
    }
}