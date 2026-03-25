package mg.mbds.webservice.config;

import mg.mbds.webservice.enums.Role;
import mg.mbds.webservice.enums.StayOutcome;
import mg.mbds.webservice.model.*;
import mg.mbds.webservice.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(
            UserRepository    userRepo,
            PatientRepository patientRepo,
            RoomRepository    roomRepo,
            StayRepository    stayRepo,
            PasswordEncoder   passwordEncoder,
            DataSource        dataSource
    ) {
        return args -> {
            if (userRepo.count() > 0) return;

            // ── 1. Execute data.sql (rooms, patients, medications) ───────
            try (Connection connection = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(connection, new ClassPathResource("data.sql"));
            }

            // ── 2. Users (BCrypt hashed at runtime) ──────────────────────
            String hash = passwordEncoder.encode("password");
            List<User> users = userRepo.saveAll(List.of(
                user("admin",   "admin@hospital.mg",   "Admin", "System", Role.ADMIN,  hash),
                user("dr.john", "john@hospital.mg",    "John",  "Doe",    Role.DOCTOR, hash),
                user("dr.jane", "jane@hospital.mg",    "Jane",  "Smith",  Role.DOCTOR, hash),
                user("nurse01", "nurse01@hospital.mg", "Marie", "Dupont", Role.NURSE,  hash),
                user("nurse02", "nurse02@hospital.mg", "Paul",  "Martin", Role.NURSE,  hash)
            ));
            User john = users.get(1);
            User jane = users.get(2);

            // ── 3. Stays (depend on saved users + patients + rooms) ──────
            List<Patient> patients = patientRepo.findAll();
            List<Room>    rooms    = roomRepo.findAll();

            stayRepo.saveAll(List.of(
                stay(LocalDate.of(2026,3, 1), LocalDate.of(2026,3,10), "Pneumonia",        "Patient responded well to antibiotics", StayOutcome.RECOVERED, null,           "dr.john", patients.get(0), rooms.get(0), john),
                stay(LocalDate.of(2026,3, 5), null,                    "Diabetes type 2",  "Monitoring blood sugar levels",         null,                  null,           "dr.jane", patients.get(1), rooms.get(2), jane),
                stay(LocalDate.of(2026,3,10), LocalDate.of(2026,3,15), "Appendicitis",     "Post-surgery recovery",                 StayOutcome.RECOVERED, null,           "dr.john", patients.get(2), rooms.get(1), john),
                stay(LocalDate.of(2026,3,18), null,                    "Cardiac arrest",   "Under close monitoring in reanimation", null,                  null,           "dr.jane", patients.get(3), rooms.get(6), jane),
                stay(LocalDate.of(2026,3,20), LocalDate.of(2026,3,22), "Severe infection", "Sepsis, did not respond to treatment",  StayOutcome.DECEASED,  "Septic shock", "dr.john", patients.get(4), rooms.get(7), john)
            ));
        };
    }

    // ── Builders ─────────────────────────────────────────────────────────

    private User user(String username, String email, String firstName, String lastName, Role role, String hash) {
        User u = new User();
        u.setUsername(username); u.setEmail(email);
        u.setFirstName(firstName); u.setLastName(lastName);
        u.setRole(role); u.setPasswordHash(hash);
        u.setActive(true); u.setCreatedAt(LocalDateTime.now());
        return u;
    }

    private Stay stay(LocalDate start, LocalDate end, String diagnosis, String notes,
                      StayOutcome outcome, String causeOfDeath, String createdBy,
                      Patient patient, Room room, User doctor) {
        Stay s = new Stay();
        s.setStartDate(start); s.setEndDate(end);
        s.setDiagnosis(diagnosis); s.setNotes(notes);
        s.setStayOutcome(outcome); s.setCauseOfDeath(causeOfDeath);
        s.setCreatedBy(createdBy); s.setCreatedAt(LocalDateTime.now());
        s.setPatient(patient); s.setRoom(room); s.setDoctor(doctor);
        return s;
    }
}
