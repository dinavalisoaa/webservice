package mg.mbds.webservice.repository;

import mg.mbds.webservice.dto.RoomOccupancyRow;
import mg.mbds.webservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(value = """
            SELECT r.id, r.number, r.type, r.capacity,
                   r.under_maintenance AS underMaintenance,
                   r.price_per_nigth   AS pricePerNight,
                   (SELECT COUNT(*) FROM stay s WHERE s.room_id = r.id AND s.end_date IS NULL) AS currentOccupancy
            FROM room r
            ORDER BY r.number
            """, nativeQuery = true)
    List<RoomOccupancyRow> findAllWithCurrentOccupancy();

    @Query(value = """
            SELECT r.capacity - COUNT(s.id) AS places
            FROM room r
            LEFT JOIN stay s ON s.room_id = r.id AND s.end_date IS NULL
            WHERE r.id = :roomId AND r.under_maintenance = FALSE
            GROUP BY r.id, r.capacity
            """, nativeQuery = true)
    Integer countAvailablePlaces(@Param("roomId") Long roomId);

    @Query(value = "SELECT COUNT(*) FROM stay WHERE room_id = :roomId AND end_date IS NULL", nativeQuery = true)
    Integer countActiveStays(@Param("roomId") Long roomId);

    @Modifying
    @Query(value = "UPDATE room SET under_maintenance = :enable WHERE id = :roomId", nativeQuery = true)
    void setMaintenance(@Param("roomId") Long roomId, @Param("enable") Boolean enable);
}
