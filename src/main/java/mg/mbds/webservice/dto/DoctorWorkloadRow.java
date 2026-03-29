package mg.mbds.webservice.dto;

public interface DoctorWorkloadRow {
    Long getId();
    String getFirstName();
    String getLastName();
    String getUsername();
    Integer getActivePatients();
    Integer getTotalPatients();
}
