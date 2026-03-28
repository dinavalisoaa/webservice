package mg.mbds.webservice.controller;

import mg.mbds.webservice.responses.SuccessResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HomeController {
    @GetMapping("/")
    public SuccessResponse<String> test() {
        return SuccessResponse.of("Hello");
    }
}
