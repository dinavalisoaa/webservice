package mg.mbds.webservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.mbds.webservice.model.User;

public interface UserRepository extends JpaRepository<User,Long> { 
    Optional<User> findByEmail(String email);
}
