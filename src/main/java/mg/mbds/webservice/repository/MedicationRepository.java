package mg.mbds.webservice.repository;

import mg.mbds.webservice.dto.MedicationStockAlertDTO;
import mg.mbds.webservice.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    @Query("""
            SELECT new mg.mbds.webservice.dto.MedicationStockAlertDTO(
                m.id, m.name, m.dosage, m.manufacturer,
                m.stock, m.alertThreshold, COUNT(pm))
            FROM Medication m
            LEFT JOIN PrescriptionMedication pm ON pm.medication = m
            GROUP BY m.id, m.name, m.dosage, m.manufacturer, m.stock, m.alertThreshold
            ORDER BY COUNT(pm) DESC
            """)
    List<MedicationStockAlertDTO> findAllWithPrescriptionCount();

    @Modifying
    @Query(value = "UPDATE medication SET stock = stock - :quantity WHERE id = :medicationId AND stock >= :quantity", nativeQuery = true)
    int decrementStock(Long medicationId, int quantity);
}
