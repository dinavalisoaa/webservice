package mg.mbds.webservice.service;

import mg.mbds.webservice.dto.RoomStatusDTO;
import mg.mbds.webservice.exception.ResourceNotFoundException;
import mg.mbds.webservice.model.Patient;
import mg.mbds.webservice.enums.RoomType;
import mg.mbds.webservice.model.Room;
import mg.mbds.webservice.repository.RoomRepository;
import mg.mbds.webservice.repository.RoomSpecification;
import mg.mbds.webservice.repository.StayRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
        LocalDate effectiveDate = date != null ? date : LocalDate.now();

        Specification<Room> spec = Specification
                .where(RoomSpecification.isAvailableOn(effectiveDate))
                .and(RoomSpecification.hasType(type))
                .and(RoomSpecification.hasMinCapacity(minCapacity))
                .and(RoomSpecification.hasPriceGreaterThanOrEqual(minPrice))
                .and(RoomSpecification.hasPriceLessThanOrEqual(maxPrice));

        return roomRepository.findAll(spec);
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
                .map(row -> {
                    Room room = (Room) row[0];
                    int occupancy = ((Long) row[1]).intValue();
                    int available = room.getCapacity() - occupancy;

                    String status;
                    if (Boolean.TRUE.equals(room.getUnderMaintenance())) {
                        status = "UNDER_MAINTENANCE";
                    } else if (available <= 0) {
                        status = "COMPLETE";
                    } else {
                        status = "AVAILABLE";
                    }

                    return new RoomStatusDTO(
                            room.getId(), room.getNumber(), room.getType(),
                            room.getCapacity(), occupancy, Math.max(available, 0),
                            room.getUnderMaintenance(), room.getPricePerNight(), status
                    );
                })
                .toList();
    }
}
