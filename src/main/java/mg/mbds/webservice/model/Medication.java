package mg.mbds.webservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 50)
    private String dosage;

    @Column(length = 100)
    private String manufacturer;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(name = "alert_threshold", nullable = false)
    private Integer alertThreshold = 50;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Boolean available = true;
}
