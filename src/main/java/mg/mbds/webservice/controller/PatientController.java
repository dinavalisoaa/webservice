package mg.mbds.webservice.controller;

import mg.mbds.webservice.dto.PatientStayDTO;
import mg.mbds.webservice.model.Patient;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.PatientService;
import mg.mbds.webservice.service.StayService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final StayService stayService;

    public PatientController(PatientService patientService, StayService stayService) {
        this.patientService = patientService;
        this.stayService = stayService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    public SuccessResponse<List<Patient>> getAll() {
        return SuccessResponse.of(patientService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    public SuccessResponse<Patient> getById(@PathVariable Long id) {
        return SuccessResponse.of(patientService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PATIENT_WRITE')")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Patient> create(@RequestBody Patient patient) {
        return SuccessResponse.of(patientService.create(patient));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT_WRITE')")
    public SuccessResponse<Patient> update(@PathVariable Long id, @RequestBody Patient patient) {
        return SuccessResponse.of(patientService.update(id, patient));
    }

    @GetMapping("/{id}/stays")
    @PreAuthorize("hasAuthority('STAY_READ')")
    public SuccessResponse<List<PatientStayDTO>> getStays(@PathVariable Long id) {
        return SuccessResponse.of(stayService.getByPatientId(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        patientService.delete(id);
    }
}
