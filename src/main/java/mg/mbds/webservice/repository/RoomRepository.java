package mg.mbds.webservice.repository;

import mg.mbds.webservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

//    Retourne toutes les chambres avec leur occupation courant
    @Query("""
            SELECT r, (SELECT COUNT(s) FROM Stay s WHERE s.room = r AND s.endDate IS NULL)
            FROM Room r
            ORDER BY r.number
            """)
    List<Object[]> findAllWithCurrentOccupancy();
}
