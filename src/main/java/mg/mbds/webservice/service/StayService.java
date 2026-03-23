package mg.mbds.webservice.service;

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
                .orElseThrow(() -> new RuntimeException("Stay not found: " + id));
    }

    public Stay create(Stay stay, Long patientId, Long roomId, Long doctorId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));
        stay.setPatient(patient);
        stay.setRoom(room);
        stay.setDoctor(doctor);
        return stayRepository.save(stay);
    }

    public Stay update(Long id, Stay updated, Long roomId, Long doctorId) {
        Stay existing = getById(id);
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setDiagnosis(updated.getDiagnosis());
        existing.setNotes(updated.getNotes());
        existing.setStayOutcome(updated.getStayOutcome());
        existing.setCauseOfDeath(updated.getCauseOfDeath());
        existing.setCreatedBy(updated.getCreatedBy());
        if (roomId != null) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));
            existing.setRoom(room);
        }
        if (doctorId != null) {
            User doctor = userRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));
            existing.setDoctor(doctor);
        }
        return stayRepository.save(existing);
    }

    public void delete(Long id) {
        stayRepository.deleteById(id);
    }
}
