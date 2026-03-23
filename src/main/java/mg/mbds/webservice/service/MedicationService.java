package mg.mbds.webservice.service;

import mg.mbds.webservice.model.Medication;
import mg.mbds.webservice.repository.MedicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> getAll() {
        return medicationRepository.findAll();
    }

    public Medication getById(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found: " + id));
    }

    public Medication create(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication update(Long id, Medication updated) {
        Medication existing = getById(id);
        existing.setName(updated.getName());
        existing.setDosage(updated.getDosage());
        existing.setManufacturer(updated.getManufacturer());
        existing.setStock(updated.getStock());
        existing.setAlertThreshold(updated.getAlertThreshold());
        existing.setPrice(updated.getPrice());
        existing.setAvailable(updated.getAvailable());
        return medicationRepository.save(existing);
    }

    public void delete(Long id) {
        medicationRepository.deleteById(id);
    }
}
