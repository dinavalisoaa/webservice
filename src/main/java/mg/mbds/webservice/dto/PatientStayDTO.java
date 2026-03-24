package mg.mbds.webservice.dto;

import lombok.Getter;
import mg.mbds.webservice.enums.RoomType;
import mg.mbds.webservice.enums.StayOutcome;
import mg.mbds.webservice.model.Stay;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PatientStayDTO {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String diagnosis;
    private String notes;
    private StayOutcome stayOutcome;
    private String causeOfDeath;
    private LocalDateTime createdAt;
    private String createdBy;

    private Long roomId;
    private String roomNumber;
    private RoomType roomType;

    private Long doctorId;
    private String doctorFirstName;
    private String doctorLastName;

    public PatientStayDTO(Stay stay) {
        this.id = stay.getId();
        this.startDate = stay.getStartDate();
        this.endDate = stay.getEndDate();
        this.diagnosis = stay.getDiagnosis();
        this.notes = stay.getNotes();
        this.stayOutcome = stay.getStayOutcome();
        this.causeOfDeath = stay.getCauseOfDeath();
        this.createdAt = stay.getCreatedAt();
        this.createdBy = stay.getCreatedBy();

        if (stay.getRoom() != null) {
            this.roomId = stay.getRoom().getId();
            this.roomNumber = stay.getRoom().getNumber();
            this.roomType = stay.getRoom().getType();
        }

        if (stay.getDoctor() != null) {
            this.doctorId = stay.getDoctor().getId();
            this.doctorFirstName = stay.getDoctor().getFirstName();
            this.doctorLastName = stay.getDoctor().getLastName();
        }
    }
}
