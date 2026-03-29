package mg.mbds.webservice.controller;

import mg.mbds.webservice.model.Prescription;
import mg.mbds.webservice.model.PrescriptionMedication;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRESCRIPTION_READ')")
    public SuccessResponse<List<Prescription>> getAll() {
        return SuccessResponse.of(prescriptionService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRESCRIPTION_READ')")
    public SuccessResponse<Prescription> getById(@PathVariable Long id) {
        return SuccessResponse.of(prescriptionService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRESCRIPTION_WRITE')")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Prescription> create(
            @RequestBody Prescription prescription,
            @RequestParam Long stayId,
            @RequestParam Long doctorId) {
        return SuccessResponse.of(prescriptionService.create(prescription, stayId, doctorId));
    }

    @PutMapping("/{id}")
    public SuccessResponse<Prescription> update(@PathVariable Long id, @RequestBody Prescription prescription) {
        return SuccessResponse.of(prescriptionService.update(id, prescription));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        prescriptionService.delete(id);
    }

    @GetMapping("/{id}/medications")
    public SuccessResponse<List<PrescriptionMedication>> getMedications(@PathVariable Long id) {
        return SuccessResponse.of(prescriptionService.getMedications(id));
    }

    @PostMapping("/{id}/medications")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<PrescriptionMedication> addMedication(
            @PathVariable Long id,
            @RequestBody PrescriptionMedication pm,
            @RequestParam Long medicationId) {
        return SuccessResponse.of(prescriptionService.addMedication(id, pm, medicationId));
    }

    @DeleteMapping("/{id}/medications/{pmId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMedication(@PathVariable Long id, @PathVariable Long pmId) {
        prescriptionService.removeMedication(id, pmId);
    }
}
