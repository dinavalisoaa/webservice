package mg.mbds.webservice.service;

import mg.mbds.webservice.dto.PatientMedicalHistoryDTO;
import mg.mbds.webservice.exception.ResourceNotFoundException;
import mg.mbds.webservice.model.Patient;
import mg.mbds.webservice.model.Prescription;
import mg.mbds.webservice.model.Stay;
import mg.mbds.webservice.repository.PatientRepository;
import mg.mbds.webservice.repository.PrescriptionMedicationRepository;
import mg.mbds.webservice.repository.PrescriptionRepository;
import mg.mbds.webservice.repository.StayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final StayRepository stayRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMedicationRepository prescriptionMedicationRepository;

    public PatientService(PatientRepository patientRepository,
                          StayRepository stayRepository,
                          PrescriptionRepository prescriptionRepository,
                          PrescriptionMedicationRepository prescriptionMedicationRepository) {
        this.patientRepository = patientRepository;
        this.stayRepository = stayRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionMedicationRepository = prescriptionMedicationRepository;
    }

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public Patient getById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
    }

    public Patient create(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient update(Long id, Patient updated) {
        Patient existing = getById(id);
        existing.setLastName(updated.getLastName());
        existing.setFirstName(updated.getFirstName());
        existing.setBirthDate(updated.getBirthDate());
        existing.setSocialSecurityNumber(updated.getSocialSecurityNumber());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setAddress(updated.getAddress());
        existing.setBloodGroup(updated.getBloodGroup());
        return patientRepository.save(existing);
    }

    public void delete(Long id) {
        patientRepository.deleteById(id);
    }

    public PatientMedicalHistoryDTO getMedicalHistory(Long patientId) {
        Patient patient = getById(patientId);
        List<PatientMedicalHistoryDTO.StayHistoryDTO> stayDTOs = stayRepository.findByPatientId(patientId)
                .stream()
                .map(this::toStayHistoryDTO)
                .toList();
        return new PatientMedicalHistoryDTO(patient, stayDTOs);
    }

    private PatientMedicalHistoryDTO.StayHistoryDTO toStayHistoryDTO(Stay stay) {
        List<PatientMedicalHistoryDTO.PrescriptionHistoryDTO> prescriptionDTOs =
                toPrescriptionHistoryDTOs(stay.getId());
        return new PatientMedicalHistoryDTO.StayHistoryDTO(stay, prescriptionDTOs);
    }

    private List<PatientMedicalHistoryDTO.PrescriptionHistoryDTO> toPrescriptionHistoryDTOs(Long stayId) {
        return prescriptionRepository.findByStayId(stayId)
                .stream()
                .map(this::toPrescriptionHistoryDTO)
                .toList();
    }

    private PatientMedicalHistoryDTO.PrescriptionHistoryDTO toPrescriptionHistoryDTO(Prescription prescription) {
        List<PatientMedicalHistoryDTO.MedicationDetailDTO> medicationDTOs =
                prescriptionMedicationRepository.findByPrescriptionId(prescription.getId())
                        .stream()
                        .map(PatientMedicalHistoryDTO.MedicationDetailDTO::new)
                        .toList();
        return new PatientMedicalHistoryDTO.PrescriptionHistoryDTO(prescription, medicationDTOs);
    }
}
