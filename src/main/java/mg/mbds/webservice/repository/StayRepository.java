package mg.mbds.webservice.repository;

import mg.mbds.webservice.model.Stay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {

    List<Stay> findByRoomIdAndEndDateIsNull(Long roomId);
}
