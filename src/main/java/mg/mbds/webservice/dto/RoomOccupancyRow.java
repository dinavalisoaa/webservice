package mg.mbds.webservice.dto;

public interface RoomOccupancyRow {
    Long getId();
    String getNumber();
    String getType();
    Integer getCapacity();
    Boolean getUnderMaintenance();
    Double getPricePerNight();
    Integer getCurrentOccupancy();
}
