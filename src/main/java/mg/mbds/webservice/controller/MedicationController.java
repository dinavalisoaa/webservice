package mg.mbds.webservice.controller;

import mg.mbds.webservice.assembler.MedicationAssembler;
import mg.mbds.webservice.dto.MedicationStockAlertDTO;
import mg.mbds.webservice.dto.RestockRequest;
import mg.mbds.webservice.model.Medication;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.MedicationService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    private final MedicationService medicationService;
    private final MedicationAssembler assembler;

    public MedicationController(MedicationService medicationService, MedicationAssembler assembler) {
        this.medicationService = medicationService;
        this.assembler         = assembler;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MEDICATION_READ')")
    public SuccessResponse<CollectionModel<EntityModel<Medication>>> getAll() {
        var medications = medicationService.getAll().stream().map(assembler::toModel).toList();
        return SuccessResponse.of(CollectionModel.of(medications,
                linkTo(methodOn(MedicationController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MEDICATION_READ')")
    public SuccessResponse<EntityModel<Medication>> getById(@PathVariable Long id) {
        return SuccessResponse.of(assembler.toModel(medicationService.getById(id)));
    }

    @GetMapping("/stock-alerts")
    @PreAuthorize("hasAuthority('MEDICATION_READ')")
    public SuccessResponse<CollectionModel<EntityModel<MedicationStockAlertDTO>>> getStockAlerts() {
        var alerts = medicationService.getStockAlerts().stream()
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(MedicationController.class).getById(dto.getId())).withRel("medication")))
                .toList();
        return SuccessResponse.of(CollectionModel.of(alerts,
                linkTo(methodOn(MedicationController.class).getStockAlerts()).withSelfRel()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MEDICATION_WRITE')")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<EntityModel<Medication>> create(@RequestBody Medication medication) {
        return SuccessResponse.of(assembler.toModel(medicationService.create(medication)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MEDICATION_WRITE')")
    public SuccessResponse<EntityModel<Medication>> update(@PathVariable Long id, @RequestBody Medication medication) {
        return SuccessResponse.of(assembler.toModel(medicationService.update(id, medication)));
    }

    @PatchMapping("/{id}/restock")
    @PreAuthorize("hasAuthority('MEDICATION_WRITE')")
    public SuccessResponse<EntityModel<Medication>> restock(@PathVariable Long id, @RequestBody RestockRequest req) {
        return SuccessResponse.of(assembler.toModel(medicationService.restock(id, req)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MEDICATION_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        medicationService.delete(id);
    }
}
