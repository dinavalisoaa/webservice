package mg.mbds.webservice.service;

import mg.mbds.webservice.dto.PatientStayDTO;
import mg.mbds.webservice.exception.ResourceNotFoundException;
import mg.mbds.webservice.model.Patient;
import mg.mbds.webservice.model.Room;
import mg.mbds.webservice.model.Stay;
import mg.mbds.webservice.model.User;
import mg.mbds.webservice.repository.PatientRepository;
import mg.mbds.webservice.repository.RoomRepository;
import mg.mbds.webservice.repository.StayRepository;
import mg.mbds.webservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StayService {

    private final StayRepository stayRepository;
    private final PatientRepository patientRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public StayService(StayRepository stayRepository,
                       PatientRepository patientRepository,
                       RoomRepository roomRepository,
                       UserRepository userRepository) {
        this.stayRepository = stayRepository;
        this.patientRepository = patientRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public List<Stay> getAll() {
        return stayRepository.findAll();
    }

    public Stay getById(Long id) {
        return stayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stay not found: " + id));
    }

    public Stay create(Stay stay, Long patientId, Long roomId, Long doctorId) {
        stay.setPatient(findPatient(patientId));
        stay.setRoom(findRoom(roomId));
        stay.setDoctor(findDoctor(doctorId));
        return stayRepository.save(stay);
    }

    public Stay update(Long id, Stay updated, Long roomId, Long doctorId) {
        Stay existing = getById(id);
        applyUpdates(existing, updated);
        if (roomId != null) existing.setRoom(findRoom(roomId));
        if (doctorId != null) existing.setDoctor(findDoctor(doctorId));
        return stayRepository.save(existing);
    }

    public List<PatientStayDTO> getByPatientId(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found: " + patientId);
        }
        return stayRepository.findByPatientId(patientId).stream()
                .map(PatientStayDTO::new)
                .toList();
    }

    public void delete(Long id) {
        stayRepository.deleteById(id);
    }

    private Patient findPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
    }

    private Room findRoom(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + id));
    }

    private User findDoctor(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + id));
    }

    private void applyUpdates(Stay existing, Stay updated) {
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setDiagnosis(updated.getDiagnosis());
        existing.setNotes(updated.getNotes());
        existing.setStayOutcome(updated.getStayOutcome());
        existing.setCauseOfDeath(updated.getCauseOfDeath());
        existing.setCreatedBy(updated.getCreatedBy());
    }
}
