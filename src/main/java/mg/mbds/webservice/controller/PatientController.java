package mg.mbds.webservice.controller;

import mg.mbds.webservice.model.Patient;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public SuccessResponse<List<Patient>> getAll() {
        return SuccessResponse.of(patientService.getAll());
    }

    @GetMapping("/{id}")
    public SuccessResponse<Patient> getById(@PathVariable Long id) {
        return SuccessResponse.of(patientService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Patient> create(@RequestBody Patient patient) {
        return SuccessResponse.of(patientService.create(patient));
    }

    @PutMapping("/{id}")
    public SuccessResponse<Patient> update(@PathVariable Long id, @RequestBody Patient patient) {
        return SuccessResponse.of(patientService.update(id, patient));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        patientService.delete(id);
    }
}
