package mg.mbds.webservice.repository;

import mg.mbds.webservice.model.Stay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {

    List<Stay> findByPatientId(Long patientId);

    List<Stay> findByRoomIdAndEndDateIsNull(Long roomId);

    @Query(value = "SELECT COUNT(*) FROM stay WHERE id = :id AND end_date IS NULL", nativeQuery = true)
    Integer isActive(@Param("id") Long id);

    @Modifying
    @Query(value = """
            UPDATE stay
            SET end_date      = CURRENT_DATE,
                stay_outcome  = :outcome,
                cause_of_death = :causeOfDeath
            WHERE id = :id AND end_date IS NULL
            """, nativeQuery = true)
    int discharge(@Param("id") Long id,
                  @Param("outcome") String outcome,
                  @Param("causeOfDeath") String causeOfDeath);

    @Modifying
    @Query(value = "UPDATE stay SET room_id = :roomId WHERE id = :id AND end_date IS NULL", nativeQuery = true)
    int transfer(@Param("id") Long id, @Param("roomId") Long roomId);
}
