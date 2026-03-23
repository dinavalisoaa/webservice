package mg.mbds.webservice.service;

import mg.mbds.webservice.model.Patient;
import mg.mbds.webservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
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
}
