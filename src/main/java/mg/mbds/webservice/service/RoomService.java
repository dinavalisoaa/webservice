package mg.mbds.webservice.service;

import mg.mbds.webservice.dto.RoomOccupancyRow;
import mg.mbds.webservice.dto.RoomStatusDTO;
import mg.mbds.webservice.exception.ResourceNotFoundException;
import mg.mbds.webservice.model.Patient;
import mg.mbds.webservice.enums.RoomType;
import mg.mbds.webservice.model.Room;
import mg.mbds.webservice.repository.RoomRepository;
import mg.mbds.webservice.repository.StayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final StayRepository stayRepository;

    public RoomService(RoomRepository roomRepository, StayRepository stayRepository) {
        this.roomRepository = roomRepository;
        this.stayRepository = stayRepository;
    }

    public List<Room> getAvailableRooms(RoomType type, Integer minCapacity,
                                        Double minPrice, Double maxPrice,
                                        LocalDate date) {
        return roomRepository.findAvailableRooms(
                date != null ? date : LocalDate.now(),
                type != null ? type.name() : null,
                minCapacity,
                minPrice,
                maxPrice
        );
    }

    public List<Patient> getCurrentPatientsInRoom(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException("Chambre introuvable : id=" + roomId);
        }
        return stayRepository.findByRoomIdAndEndDateIsNull(roomId)
                .stream()
                .map(stay -> stay.getPatient())
                .toList();
    }

    public List<RoomStatusDTO> getAllRoomStatuses() {
        return roomRepository.findAllWithCurrentOccupancy()
                .stream()
                .map(this::toStatusDTO)
                .toList();
    }

    @Transactional
    public void setMaintenance(Long roomId, Boolean enable) {
        if (!roomRepository.existsById(roomId))
            throw new ResourceNotFoundException("Chambre introuvable : " + roomId);
        if (enable && roomRepository.countActiveStays(roomId) > 0)
            throw new IllegalStateException("Impossible — des séjours sont en cours dans cette chambre");
        roomRepository.setMaintenance(roomId, enable);
    }

    private RoomStatusDTO toStatusDTO(RoomOccupancyRow r) {
        int available = Math.max(r.getCapacity() - r.getCurrentOccupancy(), 0);
        String status = r.getUnderMaintenance() ? "UNDER_MAINTENANCE"
                      : available == 0           ? "COMPLETE"
                      :                            "AVAILABLE";
        return new RoomStatusDTO(r.getId(), r.getNumber(), RoomType.valueOf(r.getType()),
                r.getCapacity(), r.getCurrentOccupancy(), available,
                r.getUnderMaintenance(), r.getPricePerNight(), status);
    }
}
