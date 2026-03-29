package mg.mbds.webservice.controller;

import mg.mbds.webservice.assembler.PrescriptionAssembler;
import mg.mbds.webservice.model.Prescription;
import mg.mbds.webservice.model.PrescriptionMedication;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.PrescriptionService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final PrescriptionAssembler assembler;

    public PrescriptionController(PrescriptionService prescriptionService, PrescriptionAssembler assembler) {
        this.prescriptionService = prescriptionService;
        this.assembler           = assembler;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRESCRIPTION_READ')")
    public SuccessResponse<CollectionModel<EntityModel<Prescription>>> getAll() {
        var prescriptions = prescriptionService.getAll().stream().map(assembler::toModel).toList();
        return SuccessResponse.of(CollectionModel.of(prescriptions,
                linkTo(methodOn(PrescriptionController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRESCRIPTION_READ')")
    public SuccessResponse<EntityModel<Prescription>> getById(@PathVariable Long id) {
        return SuccessResponse.of(assembler.toModel(prescriptionService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRESCRIPTION_WRITE')")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<EntityModel<Prescription>> create(
            @RequestBody Prescription prescription,
            @RequestParam Long stayId,
            @RequestParam Long doctorId) {
        return SuccessResponse.of(assembler.toModel(prescriptionService.create(prescription, stayId, doctorId)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRESCRIPTION_WRITE')")
    public SuccessResponse<EntityModel<Prescription>> update(@PathVariable Long id, @RequestBody Prescription prescription) {
        return SuccessResponse.of(assembler.toModel(prescriptionService.update(id, prescription)));
    }

    @GetMapping("/{id}/medications")
    @PreAuthorize("hasAuthority('PRESCRIPTION_READ')")
    public SuccessResponse<CollectionModel<EntityModel<PrescriptionMedication>>> getMedications(@PathVariable Long id) {
        var items = prescriptionService.getMedications(id).stream()
                .map(pm -> EntityModel.of(pm,
                        Link.of("/api/prescriptions/" + id + "/medications/" + pm.getId()).withSelfRel(),
                        linkTo(methodOn(PrescriptionController.class).getById(id)).withRel("prescription")))
                .toList();
        return SuccessResponse.of(CollectionModel.of(items,
                linkTo(methodOn(PrescriptionController.class).getMedications(id)).withSelfRel()));
    }

    @PostMapping("/{id}/medications")
    @PreAuthorize("hasAuthority('PRESCRIPTION_WRITE')")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<EntityModel<PrescriptionMedication>> addMedication(
            @PathVariable Long id,
            @RequestBody PrescriptionMedication pm,
            @RequestParam Long medicationId) {
        var saved = prescriptionService.addMedication(id, pm, medicationId);
        return SuccessResponse.of(EntityModel.of(saved,
                Link.of("/api/prescriptions/" + id + "/medications/" + saved.getId()).withSelfRel(),
                linkTo(methodOn(PrescriptionController.class).getById(id)).withRel("prescription")));
    }

    @PatchMapping("/{id}/dispense")
    @PreAuthorize("hasAuthority('PRESCRIPTION_WRITE')")
    public SuccessResponse<EntityModel<Prescription>> dispense(@PathVariable Long id) {
        return SuccessResponse.of(assembler.toModel(prescriptionService.dispense(id)));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('PRESCRIPTION_WRITE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        prescriptionService.cancel(id);
    }

    @DeleteMapping("/{id}/medications/{pmId}")
    @PreAuthorize("hasAuthority('PRESCRIPTION_WRITE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMedication(@PathVariable Long id, @PathVariable Long pmId) {
        prescriptionService.removeMedication(id, pmId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRESCRIPTION_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        prescriptionService.delete(id);
    }
}
