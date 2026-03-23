package mg.mbds.webservice.service;

import mg.mbds.webservice.model.Room;
import mg.mbds.webservice.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findAvailableRooms();
    }
}
