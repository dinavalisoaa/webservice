package mg.mbds.webservice.repository;

import mg.mbds.webservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = """
            SELECT r.* FROM room r
            WHERE r.under_maintenance = false
              AND (SELECT COUNT(*) FROM stay s
                   WHERE s.room_id = r.id
                     AND s.start_date <= :date
                     AND (s.end_date IS NULL OR s.end_date >= :date)) < r.capacity
              AND (:type     IS NULL OR r.type           = :type)
              AND (:minCap   IS NULL OR r.capacity       >= :minCap)
              AND (:minPrice IS NULL OR r.price_per_nigth >= :minPrice)
              AND (:maxPrice IS NULL OR r.price_per_nigth <= :maxPrice)
            ORDER BY r.number
            """, nativeQuery = true)
    List<Room> findAvailableRooms(@Param("date")     LocalDate date,
                                  @Param("type")     String type,
                                  @Param("minCap")   Integer minCapacity,
                                  @Param("minPrice") Double minPrice,
                                  @Param("maxPrice") Double maxPrice);

    @Query("""
            SELECT r, (SELECT COUNT(s) FROM Stay s WHERE s.room = r AND s.endDate IS NULL)
            FROM Room r
            ORDER BY r.number
            """)
    List<Object[]> findAllWithCurrentOccupancy();
}
