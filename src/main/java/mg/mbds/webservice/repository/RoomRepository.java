package mg.mbds.webservice.repository;

import mg.mbds.webservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("""
            SELECT r FROM Room r
            WHERE r.underMaintenance = false
            AND (SELECT COUNT(s) FROM Stay s WHERE s.room = r AND s.endDate IS NULL) < r.capacity
            """)
    List<Room> findAvailableRooms();
}
