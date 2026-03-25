package mg.mbds.webservice.dto;

import lombok.Getter;
import mg.mbds.webservice.enums.AdministrationRoute;
import mg.mbds.webservice.enums.BloodGroup;
import mg.mbds.webservice.enums.PrescriptionStatus;
import mg.mbds.webservice.enums.RoomType;
import mg.mbds.webservice.enums.StayOutcome;
import mg.mbds.webservice.model.Patient;
import mg.mbds.webservice.model.Prescription;
import mg.mbds.webservice.model.PrescriptionMedication;
import mg.mbds.webservice.model.Stay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PatientMedicalHistoryDTO {

    private final Long id;
    private final String lastName;
    private final String firstName;
    private final LocalDate birthDate;
    private final String socialSecurityNumber;
    private final String phoneNumber;
    private final String address;
    private final BloodGroup bloodGroup;
    private final LocalDateTime createdAt;
    private final List<StayHistoryDTO> stays;

    public PatientMedicalHistoryDTO(Patient patient, List<StayHistoryDTO> stays) {
        this.id = patient.getId();
        this.lastName = patient.getLastName();
        this.firstName = patient.getFirstName();
        this.birthDate = patient.getBirthDate();
        this.socialSecurityNumber = patient.getSocialSecurityNumber();
        this.phoneNumber = patient.getPhoneNumber();
        this.address = patient.getAddress();
        this.bloodGroup = patient.getBloodGroup();
        this.createdAt = patient.getCreatedAt();
        this.stays = stays;
    }

    @Getter
    public static class StayHistoryDTO {

        private final Long id;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String diagnosis;
        private final String notes;
        private final StayOutcome stayOutcome;
        private final String causeOfDeath;
        private final LocalDateTime createdAt;
        private final String createdBy;

        private final Long roomId;
        private final String roomNumber;
        private final RoomType roomType;

        private final Long doctorId;
        private final String doctorFirstName;
        private final String doctorLastName;

        private final List<PrescriptionHistoryDTO> prescriptions;

        public StayHistoryDTO(Stay stay, List<PrescriptionHistoryDTO> prescriptions) {
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
            } else {
                this.roomId = null;
                this.roomNumber = null;
                this.roomType = null;
            }

            if (stay.getDoctor() != null) {
                this.doctorId = stay.getDoctor().getId();
                this.doctorFirstName = stay.getDoctor().getFirstName();
                this.doctorLastName = stay.getDoctor().getLastName();
            } else {
                this.doctorId = null;
                this.doctorFirstName = null;
                this.doctorLastName = null;
            }

            this.prescriptions = prescriptions;
        }
    }

    @Getter
    public static class PrescriptionHistoryDTO {

        private final Long id;
        private final LocalDate prescriptionDate;
        private final String instructions;
        private final String duration;
        private final PrescriptionStatus status;
        private final LocalDateTime createdAt;

        private final Long doctorId;
        private final String doctorFirstName;
        private final String doctorLastName;

        private final List<MedicationDetailDTO> medications;

        public PrescriptionHistoryDTO(Prescription prescription, List<MedicationDetailDTO> medications) {
            this.id = prescription.getId();
            this.prescriptionDate = prescription.getPrescriptionDate();
            this.instructions = prescription.getInstructions();
            this.duration = prescription.getDuration();
            this.status = prescription.getStatus();
            this.createdAt = prescription.getCreatedAt();

            if (prescription.getDoctor() != null) {
                this.doctorId = prescription.getDoctor().getId();
                this.doctorFirstName = prescription.getDoctor().getFirstName();
                this.doctorLastName = prescription.getDoctor().getLastName();
            } else {
                this.doctorId = null;
                this.doctorFirstName = null;
                this.doctorLastName = null;
            }

            this.medications = medications;
        }
    }

    @Getter
    public static class MedicationDetailDTO {

        private final Long prescriptionMedicationId;
        private final Integer quantity;
        private final String frequency;
        private final AdministrationRoute administrationRoute;

        private final Long medicationId;
        private final String medicationName;
        private final String dosage;
        private final String manufacturer;

        public MedicationDetailDTO(PrescriptionMedication pm) {
            this.prescriptionMedicationId = pm.getId();
            this.quantity = pm.getQuantity();
            this.frequency = pm.getFrequency();
            this.administrationRoute = pm.getAdministrationRoute();

            if (pm.getMedication() != null) {
                this.medicationId = pm.getMedication().getId();
                this.medicationName = pm.getMedication().getName();
                this.dosage = pm.getMedication().getDosage();
                this.manufacturer = pm.getMedication().getManufacturer();
            } else {
                this.medicationId = null;
                this.medicationName = null;
                this.dosage = null;
                this.manufacturer = null;
            }
        }
    }
}
