package mg.mbds.webservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mg.mbds.webservice.enums.RoomType;

@Getter
@AllArgsConstructor
public class RoomStatusDTO {

    private Long id;
    private String number;
    private RoomType type;
    private Integer capacity;
    private int currentOccupancy;
    private int availableSlots;
    private boolean underMaintenance;
    private Double pricePerNight;
    private String status; // AVAILABLE | COMPLETE | UNDER_MAINTENANCE
}
