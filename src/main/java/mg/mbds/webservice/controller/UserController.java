package mg.mbds.webservice.controller;

import mg.mbds.webservice.model.User;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public SuccessResponse<List<User>> getAll() {
        return SuccessResponse.of(userService.getAll());
    }

    @GetMapping("/{id}")
    public SuccessResponse<User> getById(@PathVariable Long id) {
        return SuccessResponse.of(userService.getById(id));
    }

    @PutMapping("/{id}")
    public SuccessResponse<User> update(@PathVariable Long id, @RequestBody User user) {
        return SuccessResponse.of(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
