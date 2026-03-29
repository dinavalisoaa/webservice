package mg.mbds.webservice.repository;

import mg.mbds.webservice.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByStayId(Long stayId);

    @Modifying
    @Query(value = """
            UPDATE prescription SET status = 'DONE'
            WHERE stay_id = :stayId AND status = 'ACTIVE'
            """, nativeQuery = true)
    int closeAllByStay(@Param("stayId") Long stayId);

    @Modifying
    @Query(value = "UPDATE prescription SET status = 'CANCELLED' WHERE id = :id AND status = 'ACTIVE'",
           nativeQuery = true)
    int cancel(@Param("id") Long id);
}
