package mg.mbds.webservice.service;

import mg.mbds.webservice.dto.DischargeRequest;
import mg.mbds.webservice.dto.PatientStayDTO;
import mg.mbds.webservice.dto.TransferRequest;
import mg.mbds.webservice.exception.ResourceNotFoundException;
import mg.mbds.webservice.model.Patient;
import mg.mbds.webservice.model.Room;
import mg.mbds.webservice.model.Stay;
import mg.mbds.webservice.model.User;
import mg.mbds.webservice.repository.PatientRepository;
import mg.mbds.webservice.repository.PrescriptionRepository;
import mg.mbds.webservice.repository.RoomRepository;
import mg.mbds.webservice.repository.StayRepository;
import mg.mbds.webservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StayService {

    private final StayRepository stayRepository;
    private final PatientRepository patientRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PrescriptionRepository prescriptionRepository;

    public StayService(StayRepository stayRepository, PatientRepository patientRepository,
                       RoomRepository roomRepository, UserRepository userRepository,
                       PrescriptionRepository prescriptionRepository) {
        this.stayRepository = stayRepository;
        this.patientRepository = patientRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<Stay> getAll() {
        return stayRepository.findAll();
    }

    public Stay getById(Long id) {
        return stayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Séjour introuvable : " + id));
    }

    public Stay create(Stay stay, Long patientId, Long roomId, Long doctorId) {
        stay.setPatient(findPatient(patientId));
        stay.setRoom(findRoom(roomId));
        stay.setDoctor(findDoctor(doctorId));
        return stayRepository.save(stay);
    }

    public Stay update(Long id, Stay updated, Long roomId, Long doctorId) {
        Stay stay = getById(id);
        stay.setStartDate(updated.getStartDate());
        stay.setEndDate(updated.getEndDate());
        stay.setDiagnosis(updated.getDiagnosis());
        stay.setNotes(updated.getNotes());
        stay.setStayOutcome(updated.getStayOutcome());
        stay.setCauseOfDeath(updated.getCauseOfDeath());
        if (roomId != null)   stay.setRoom(findRoom(roomId));
        if (doctorId != null) stay.setDoctor(findDoctor(doctorId));
        return stayRepository.save(stay);
    }

    public void delete(Long id) {
        stayRepository.deleteById(id);
    }

    @Transactional
    public Stay discharge(Long id, DischargeRequest req) {
        if (stayRepository.isActive(id) == 0)
            throw new IllegalStateException("Séjour déjà clôturé : " + id);
        if ("DECEASED".equals(req.getOutcome()) && req.getCauseOfDeath() == null)
            throw new IllegalStateException("Cause du décès obligatoire pour outcome DECEASED");
        stayRepository.discharge(id, req.getOutcome(), req.getCauseOfDeath());
        prescriptionRepository.closeAllByStay(id);
        return getById(id);
    }

    @Transactional
    public Stay transfer(Long id, TransferRequest req) {
        if (stayRepository.isActive(id) == 0)
            throw new IllegalStateException("Impossible de transférer un séjour clôturé");
        Integer places = roomRepository.countAvailablePlaces(req.getNewRoomId());
        if (places == null)
            throw new ResourceNotFoundException("Chambre introuvable ou en maintenance : " + req.getNewRoomId());
        if (places == 0)
            throw new IllegalStateException("Chambre complète : " + req.getNewRoomId());
        stayRepository.transfer(id, req.getNewRoomId());
        return getById(id);
    }

    public List<PatientStayDTO> getByPatientId(Long patientId) {
        if (!patientRepository.existsById(patientId))
            throw new ResourceNotFoundException("Patient introuvable : " + patientId);
        return stayRepository.findByPatientId(patientId).stream()
                .map(PatientStayDTO::new)
                .toList();
    }

    private Patient findPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient introuvable : " + id));
    }

    private Room findRoom(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chambre introuvable : " + id));
    }

    private User findDoctor(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médecin introuvable : " + id));
    }
}
