package mg.mbds.webservice.repository;

import mg.mbds.webservice.dto.DoctorWorkloadRow;
import mg.mbds.webservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(value = """
            SELECT u.id, u.first_name AS firstName, u.last_name AS lastName, u.username,
                   COUNT(DISTINCT CASE WHEN s.end_date IS NULL THEN s.id END) AS activePatients,
                   COUNT(DISTINCT s.id) AS totalPatients
            FROM user u
            LEFT JOIN stay s ON s.doctor_id = u.id
            WHERE u.role = 'DOCTOR' AND u.active = TRUE
            GROUP BY u.id, u.first_name, u.last_name, u.username
            ORDER BY activePatients DESC
            """, nativeQuery = true)
    List<DoctorWorkloadRow> findDoctorWorkloads();
}
