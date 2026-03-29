package mg.mbds.webservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.mbds.webservice.enums.Role;

@Getter @Setter
@NoArgsConstructor
public class AssignRoleDTO {
    private Role role;
}
