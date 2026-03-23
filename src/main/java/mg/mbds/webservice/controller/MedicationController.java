package mg.mbds.webservice.controller;

import mg.mbds.webservice.model.Medication;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.MedicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public SuccessResponse<List<Medication>> getAll() {
        return SuccessResponse.of(medicationService.getAll());
    }

    @GetMapping("/{id}")
    public SuccessResponse<Medication> getById(@PathVariable Long id) {
        return SuccessResponse.of(medicationService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Medication> create(@RequestBody Medication medication) {
        return SuccessResponse.of(medicationService.create(medication));
    }

    @PutMapping("/{id}")
    public SuccessResponse<Medication> update(@PathVariable Long id, @RequestBody Medication medication) {
        return SuccessResponse.of(medicationService.update(id, medication));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        medicationService.delete(id);
    }
}
