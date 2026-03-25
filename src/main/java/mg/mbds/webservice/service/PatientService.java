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
                .orElseThrow(() -> new RuntimeException("Patient not found: " + id));
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
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));

        List<Stay> stays = stayRepository.findByPatientId(patientId);

        List<PatientMedicalHistoryDTO.StayHistoryDTO> stayDTOs = stays.stream()
                .map(stay -> {
                    List<Prescription> prescriptions = prescriptionRepository.findByStayId(stay.getId());

                    List<PatientMedicalHistoryDTO.PrescriptionHistoryDTO> prescriptionDTOs = prescriptions.stream()
                            .map(prescription -> {
                                List<PatientMedicalHistoryDTO.MedicationDetailDTO> medicationDTOs =
                                        prescriptionMedicationRepository.findByPrescriptionId(prescription.getId())
                                                .stream()
                                                .map(PatientMedicalHistoryDTO.MedicationDetailDTO::new)
                                                .toList();
                                return new PatientMedicalHistoryDTO.PrescriptionHistoryDTO(prescription, medicationDTOs);
                            })
                            .toList();

                    return new PatientMedicalHistoryDTO.StayHistoryDTO(stay, prescriptionDTOs);
                })
                .toList();

        return new PatientMedicalHistoryDTO(patient, stayDTOs);
    }
}
