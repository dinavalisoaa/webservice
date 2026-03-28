package mg.mbds.webservice.service;

import mg.mbds.webservice.exception.ResourceNotFoundException;
import mg.mbds.webservice.model.Medication;
import mg.mbds.webservice.model.Prescription;
import mg.mbds.webservice.model.PrescriptionMedication;
import mg.mbds.webservice.model.Stay;
import mg.mbds.webservice.model.User;
import mg.mbds.webservice.repository.MedicationRepository;
import mg.mbds.webservice.repository.PrescriptionMedicationRepository;
import mg.mbds.webservice.repository.PrescriptionRepository;
import mg.mbds.webservice.repository.StayRepository;
import mg.mbds.webservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMedicationRepository prescriptionMedicationRepository;
    private final StayRepository stayRepository;
    private final UserRepository userRepository;
    private final MedicationRepository medicationRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               PrescriptionMedicationRepository prescriptionMedicationRepository,
                               StayRepository stayRepository,
                               UserRepository userRepository,
                               MedicationRepository medicationRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionMedicationRepository = prescriptionMedicationRepository;
        this.stayRepository = stayRepository;
        this.userRepository = userRepository;
        this.medicationRepository = medicationRepository;
    }

    public List<Prescription> getAll() {
        return prescriptionRepository.findAll();
    }

    public Prescription getById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found: " + id));
    }

    public Prescription create(Prescription prescription, Long stayId, Long doctorId) {
        prescription.setStay(findStay(stayId));
        prescription.setDoctor(findDoctor(doctorId));
        return prescriptionRepository.save(prescription);
    }

    public Prescription update(Long id, Prescription updated) {
        Prescription existing = getById(id);
        existing.setPrescriptionDate(updated.getPrescriptionDate());
        existing.setInstructions(updated.getInstructions());
        existing.setDuration(updated.getDuration());
        existing.setStatus(updated.getStatus());
        return prescriptionRepository.save(existing);
    }

    public void delete(Long id) {
        prescriptionRepository.deleteById(id);
    }

    public List<PrescriptionMedication> getMedications(Long prescriptionId) {
        getById(prescriptionId);
        return prescriptionMedicationRepository.findByPrescriptionId(prescriptionId);
    }

    public PrescriptionMedication addMedication(Long prescriptionId, PrescriptionMedication pm, Long medicationId) {
        pm.setPrescription(getById(prescriptionId));
        pm.setMedication(findMedication(medicationId));
        return prescriptionMedicationRepository.save(pm);
    }

    public void removeMedication(Long prescriptionId, Long pmId) {
        PrescriptionMedication pm = prescriptionMedicationRepository.findById(pmId)
                .orElseThrow(() -> new ResourceNotFoundException("PrescriptionMedication not found: " + pmId));
        if (!pm.getPrescription().getId().equals(prescriptionId)) {
            throw new IllegalArgumentException("PrescriptionMedication does not belong to prescription: " + prescriptionId);
        }
        prescriptionMedicationRepository.deleteById(pmId);
    }

    private Stay findStay(Long id) {
        return stayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stay not found: " + id));
    }

    private User findDoctor(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + id));
    }

    private Medication findMedication(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found: " + id));
    }
}
