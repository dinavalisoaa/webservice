package mg.mbds.webservice.model;

import jakarta.persistence.*;
import lombok.*;
import mg.mbds.webservice.enums.AdministrationRoute;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prescription_medication", uniqueConstraints = {
    @UniqueConstraint(name = "uq_pm_prescription_medicament", columnNames = {"prescription_id", "medication_id"})
})
public class PrescriptionMedication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(nullable = false, length = 50)
    private String frequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "administration_route", nullable = false, length = 5)
    private AdministrationRoute administrationRoute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;
}
