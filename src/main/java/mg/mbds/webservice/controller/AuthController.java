package mg.mbds.webservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import mg.mbds.webservice.dto.LoginDTO;
import mg.mbds.webservice.dto.RegisterDTO;
import mg.mbds.webservice.model.User;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@SecurityRequirements
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public SuccessResponse<String> login(@RequestBody LoginDTO loginDto) {
        return SuccessResponse.of(authService.login(loginDto.getEmail(), loginDto.getPassword()));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<User> register(@RequestBody RegisterDTO registerDTO) {
        return SuccessResponse.of(authService.register(registerDTO));
    }
}


