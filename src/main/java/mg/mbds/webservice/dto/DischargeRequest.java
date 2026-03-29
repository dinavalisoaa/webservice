package mg.mbds.webservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DischargeRequest {
    private String outcome;
    private String causeOfDeath;
}
