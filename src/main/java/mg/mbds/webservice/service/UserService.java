package mg.mbds.webservice.service;

import mg.mbds.webservice.dto.DoctorWorkloadDTO;
import mg.mbds.webservice.enums.Role;
import mg.mbds.webservice.enums.WorkloadLevel;
import mg.mbds.webservice.model.User;
import mg.mbds.webservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public User update(Long id, User updated) {
        User existing = getById(id);
        existing.setUsername(updated.getUsername());
        existing.setEmail(updated.getEmail());
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setRole(updated.getRole());
        existing.setActive(updated.getActive());
        return userRepository.save(existing);
    }

    public User assignRole(Long id, Role role) {
        User user = getById(id);
        user.setRole(role);
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<DoctorWorkloadDTO> getDoctorWorkloads() {
        List<DoctorWorkloadDTO> doctors = userRepository.findDoctorWorkloads()
                .stream()
                .map(DoctorWorkloadDTO::new)
                .toList();

        double avg = doctors.stream()
                .mapToInt(DoctorWorkloadDTO::getActivePatients)
                .average()
                .orElse(0);

        doctors.forEach(d -> d.setWorkloadLevel(classifyWorkload(d.getActivePatients(), avg)));
        return doctors;
    }

    private WorkloadLevel classifyWorkload(int activePatients, double avg) {
        if (avg == 0)               return WorkloadLevel.NORMAL;
        if (activePatients > avg * 1.5) return WorkloadLevel.SURCHARGE;
        if (activePatients < avg * 0.5) return WorkloadLevel.SOUS_CHARGE;
        return WorkloadLevel.NORMAL;
    }
}
