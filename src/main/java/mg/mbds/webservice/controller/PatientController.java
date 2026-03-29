package mg.mbds.webservice.controller;

import mg.mbds.webservice.assembler.PatientAssembler;
import mg.mbds.webservice.dto.PatientMedicalHistoryDTO;
import mg.mbds.webservice.dto.PatientStayDTO;
import mg.mbds.webservice.model.Patient;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.PatientService;
import mg.mbds.webservice.service.StayService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final StayService stayService;
    private final PatientAssembler assembler;

    public PatientController(PatientService patientService, StayService stayService, PatientAssembler assembler) {
        this.patientService = patientService;
        this.stayService    = stayService;
        this.assembler      = assembler;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    public SuccessResponse<CollectionModel<EntityModel<Patient>>> getAll() {
        var patients = patientService.getAll().stream().map(assembler::toModel).toList();
        return SuccessResponse.of(CollectionModel.of(patients,
                linkTo(methodOn(PatientController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    public SuccessResponse<EntityModel<Patient>> getById(@PathVariable Long id) {
        return SuccessResponse.of(assembler.toModel(patientService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PATIENT_WRITE')")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<EntityModel<Patient>> create(@RequestBody Patient patient) {
        return SuccessResponse.of(assembler.toModel(patientService.create(patient)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT_WRITE')")
    public SuccessResponse<EntityModel<Patient>> update(@PathVariable Long id, @RequestBody Patient patient) {
        return SuccessResponse.of(assembler.toModel(patientService.update(id, patient)));
    }

    @GetMapping("/{id}/stays")
    @PreAuthorize("hasAuthority('STAY_READ')")
    public SuccessResponse<CollectionModel<EntityModel<PatientStayDTO>>> getStays(@PathVariable Long id) {
        var stays = stayService.getByPatientId(id).stream()
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(PatientController.class).getById(id)).withRel("patient")))
                .toList();
        return SuccessResponse.of(CollectionModel.of(stays,
                linkTo(methodOn(PatientController.class).getStays(id)).withSelfRel()));
    }
    @GetMapping("/{id}/medical-history")
    public SuccessResponse<PatientMedicalHistoryDTO> getMedicalHistory(@PathVariable Long id) {
        return SuccessResponse.of(patientService.getMedicalHistory(id));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        patientService.delete(id);
    }
}
