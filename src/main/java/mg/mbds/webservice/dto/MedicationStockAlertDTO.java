package mg.mbds.webservice.dto;

import mg.mbds.webservice.enums.StockAlertLevel;

public class MedicationStockAlertDTO {
    private Long id;
    private String name;
    private String dosage;
    private String manufacturer;
    private Integer stock;
    private Integer alertThreshold;
    private Long prescriptionCount;
    private StockAlertLevel stockAlertLevel;

    public MedicationStockAlertDTO(Long id, String name, String dosage, String manufacturer,
                                   Integer stock, Integer alertThreshold, Long prescriptionCount) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.manufacturer = manufacturer;
        this.stock = stock;
        this.alertThreshold = alertThreshold;
        this.prescriptionCount = prescriptionCount;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDosage() { return dosage; }
    public String getManufacturer() { return manufacturer; }
    public Integer getStock() { return stock; }
    public Integer getAlertThreshold() { return alertThreshold; }
    public Long getPrescriptionCount() { return prescriptionCount; }
    public StockAlertLevel getStockAlertLevel() { return stockAlertLevel; }
    public void setStockAlertLevel(StockAlertLevel stockAlertLevel) { this.stockAlertLevel = stockAlertLevel; }
}
