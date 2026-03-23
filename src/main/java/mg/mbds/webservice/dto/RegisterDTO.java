package mg.mbds.webservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.mbds.webservice.model.Role;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
    private Role role;
}
