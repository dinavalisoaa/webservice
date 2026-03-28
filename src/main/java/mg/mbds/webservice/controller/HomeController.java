package mg.mbds.webservice.controller;

import mg.mbds.webservice.dto.LoginDTO;
import mg.mbds.webservice.dto.RegisterDTO;
import mg.mbds.webservice.model.User;
import mg.mbds.webservice.repository.UserRepository;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HomeController {
    @GetMapping("/")
    public SuccessResponse<String> test() {
        return SuccessResponse. of("Hello");
    }
}