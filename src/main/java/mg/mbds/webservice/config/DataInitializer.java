package mg.mbds.webservice.config;

import mg.mbds.webservice.enums.AdministrationRoute;
import mg.mbds.webservice.enums.PrescriptionStatus;
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
            UserRepository                  userRepo,
            PatientRepository               patientRepo,
            RoomRepository                  roomRepo,
            StayRepository                  stayRepo,
            MedicationRepository            medicationRepo,
            PrescriptionRepository          prescriptionRepo,
            PrescriptionMedicationRepository pmRepo,
            PasswordEncoder                 passwordEncoder,
            DataSource                      dataSource
    ) {
        return args -> {
            if (userRepo.count() > 0) return;

            try (Connection connection = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(connection, new ClassPathResource("data.sql"));
            }

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

            List<Patient>    patients = patientRepo.findAll();
            List<Room>       rooms    = roomRepo.findAll();
            List<Medication> meds     = medicationRepo.findAll();

            List<Stay> stays = stayRepo.saveAll(List.of(
                stay(LocalDate.of(2026,3, 1), LocalDate.of(2026,3,10), "Pneumonia",           "Patient responded well to antibiotics",   StayOutcome.RECOVERED, null,           "dr.john", patients.get(0), rooms.get(0), john),
                stay(LocalDate.of(2026,3, 5), null,                    "Diabetes type 2",     "Monitoring blood sugar levels",           null,                  null,           "dr.jane", patients.get(1), rooms.get(2), jane),
                stay(LocalDate.of(2026,3,10), LocalDate.of(2026,3,15), "Appendicitis",        "Post-surgery recovery",                   StayOutcome.RECOVERED, null,           "dr.john", patients.get(2), rooms.get(1), john),
                stay(LocalDate.of(2026,3,18), null,                    "Cardiac arrest",      "Under close monitoring in reanimation",   null,                  null,           "dr.jane", patients.get(3), rooms.get(6), jane),
                stay(LocalDate.of(2026,3,20), LocalDate.of(2026,3,22), "Severe infection",    "Sepsis, did not respond to treatment",    StayOutcome.DECEASED,  "Septic shock", "dr.john", patients.get(4), rooms.get(7), john),
                stay(LocalDate.of(2026,3,22), null,                    "Hypertension crisis", "Blood pressure stabilised, monitoring",   null,                  null,           "dr.john", patients.get(5), rooms.get(3), john),
                stay(LocalDate.of(2026,3,24), null,                    "Acute bronchitis",    "Fever under control, antibiotics started", null,                 null,           "dr.jane", patients.get(6), rooms.get(4), jane),
                stay(LocalDate.of(2026,3,25), LocalDate.of(2026,3,27), "Gastric ulcer",       "Discharged after endoscopy",              StayOutcome.RECOVERED, null,           "dr.john", patients.get(7), rooms.get(1), john)
            ));

            Prescription p1 = prescriptionRepo.save(prescription(
                LocalDate.of(2026,3,1), "Take with food. Complete the full course.", "7 days", PrescriptionStatus.DONE, stays.get(0), john));
            pmRepo.saveAll(List.of(
                pm(2, "3x per day", AdministrationRoute.ORAL, p1, meds.get(1)),
                pm(2, "3x per day", AdministrationRoute.ORAL, p1, meds.get(0))
            ));

            Prescription p2 = prescriptionRepo.save(prescription(
                LocalDate.of(2026,3,5), "Take Metformin with meals. Insulin before breakfast.", "30 days", PrescriptionStatus.ACTIVE, stays.get(1), jane));
            pmRepo.saveAll(List.of(
                pm(1, "2x per day", AdministrationRoute.ORAL,         p2, meds.get(3)),
                pm(1, "1x per day", AdministrationRoute.SUBCUTANEOUS, p2, meds.get(6))
            ));

            Prescription p3 = prescriptionRepo.save(prescription(
                LocalDate.of(2026,3,10), "Post-op pain management. Do not exceed dose.", "5 days", PrescriptionStatus.DONE, stays.get(2), john));
            pmRepo.saveAll(List.of(
                pm(1, "PRN (max 4h)", AdministrationRoute.INTRAVENOUS, p3, meds.get(5)),
                pm(1, "1x per day",   AdministrationRoute.ORAL,        p3, meds.get(4))
            ));

            Prescription p4 = prescriptionRepo.save(prescription(
                LocalDate.of(2026,3,18), "Continuous monitoring required. Adjust dose per vitals.", "14 days", PrescriptionStatus.ACTIVE, stays.get(3), jane));
            pmRepo.saveAll(List.of(
                pm(1, "Continuous infusion", AdministrationRoute.INTRAVENOUS, p4, meds.get(5)),
                pm(1, "1x per day",          AdministrationRoute.ORAL,        p4, meds.get(8)),
                pm(1, "1x per day",          AdministrationRoute.INTRAVENOUS, p4, meds.get(10))
            ));

            Prescription p5 = prescriptionRepo.save(prescription(
                LocalDate.of(2026,3,20), "IV broad-spectrum antibiotics. Reassess in 48h.", "10 days", PrescriptionStatus.CANCELLED, stays.get(4), john));
            pmRepo.saveAll(List.of(
                pm(1, "2x per day", AdministrationRoute.INTRAVENOUS, p5, meds.get(7)),
                pm(1, "1x per day", AdministrationRoute.INTRAVENOUS, p5, meds.get(10))
            ));

            Prescription p6 = prescriptionRepo.save(prescription(
                LocalDate.of(2026,3,22), "Monitor BP twice daily. Reduce sodium intake.", "30 days", PrescriptionStatus.ACTIVE, stays.get(5), john));
            pmRepo.saveAll(List.of(
                pm(1, "1x per day", AdministrationRoute.ORAL, p6, meds.get(8)),
                pm(1, "1x per day", AdministrationRoute.ORAL, p6, meds.get(9))
            ));

            Prescription p7 = prescriptionRepo.save(prescription(
                LocalDate.of(2026,3,24), "Complete the antibiotic course even if symptoms improve.", "7 days", PrescriptionStatus.ACTIVE, stays.get(6), jane));
            pmRepo.saveAll(List.of(
                pm(2, "3x per day", AdministrationRoute.ORAL,        p7, meds.get(1)),
                pm(2, "3x per day", AdministrationRoute.ORAL,        p7, meds.get(0)),
                pm(1, "2x per day", AdministrationRoute.INTRAVENOUS, p7, meds.get(10))
            ));

            Prescription p8 = prescriptionRepo.save(prescription(
                LocalDate.of(2026,3,25), "Avoid NSAIDs. Follow gastric diet.", "14 days", PrescriptionStatus.DONE, stays.get(7), john));
            pmRepo.saveAll(List.of(
                pm(1, "1x per day (before meal)", AdministrationRoute.ORAL, p8, meds.get(4)),
                pm(1, "1x per day",               AdministrationRoute.ORAL, p8, meds.get(11))
            ));
        };
    }

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

    private Prescription prescription(LocalDate date, String instructions, String duration,
                                      PrescriptionStatus status, Stay stay, User doctor) {
        Prescription p = new Prescription();
        p.setPrescriptionDate(date);
        p.setInstructions(instructions);
        p.setDuration(duration);
        p.setStatus(status);
        p.setStay(stay);
        p.setDoctor(doctor);
        return p;
    }

    private PrescriptionMedication pm(int quantity, String frequency,
                                      AdministrationRoute route,
                                      Prescription prescription, Medication medication) {
        PrescriptionMedication pm = new PrescriptionMedication();
        pm.setQuantity(quantity);
        pm.setFrequency(frequency);
        pm.setAdministrationRoute(route);
        pm.setPrescription(prescription);
        pm.setMedication(medication);
        return pm;
    }
}
