package mg.mbds.webservice.service;

import mg.mbds.webservice.model.Room;
import mg.mbds.webservice.model.RoomType;
import mg.mbds.webservice.repository.RoomRepository;
import mg.mbds.webservice.repository.RoomSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
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
}
