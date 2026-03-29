package mg.mbds.webservice.dto;

import lombok.Getter;
import lombok.Setter;
import mg.mbds.webservice.enums.WorkloadLevel;

@Getter @Setter
public class DoctorWorkloadDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private int activePatients;
    private int totalPatients;
    private WorkloadLevel workloadLevel;

    public DoctorWorkloadDTO(DoctorWorkloadRow row) {
        this.id             = row.getId();
        this.firstName      = row.getFirstName();
        this.lastName       = row.getLastName();
        this.username       = row.getUsername();
        this.activePatients = row.getActivePatients();
        this.totalPatients  = row.getTotalPatients();
    }
}
