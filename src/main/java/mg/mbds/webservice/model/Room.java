package mg.mbds.webservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoomType type;

    @Column(nullable = false)
    private Integer capacity = 1;

    @Column(name = "under_maintenance", nullable = false)
    private Boolean underMaintenance = false;

    @Column(name = "price_per_nigth", nullable = false)
    private Double pricePerNight;

    @Column(name = "occupancy_alert_threshold", nullable = false)
    private Integer occupancyAlertThreshold = 80;
}
