package mg.mbds.webservice.controller;

import mg.mbds.webservice.assembler.StayAssembler;
import mg.mbds.webservice.dto.DischargeRequest;
import mg.mbds.webservice.dto.TransferRequest;
import mg.mbds.webservice.model.Stay;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.StayService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/stays")
public class StayController {

    private final StayService stayService;
    private final StayAssembler assembler;

    public StayController(StayService stayService, StayAssembler assembler) {
        this.stayService = stayService;
        this.assembler   = assembler;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('STAY_READ')")
    public SuccessResponse<CollectionModel<EntityModel<Stay>>> getAll() {
        var stays = stayService.getAll().stream().map(assembler::toModel).toList();
        return SuccessResponse.of(CollectionModel.of(stays,
                linkTo(methodOn(StayController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STAY_READ')")
    public SuccessResponse<EntityModel<Stay>> getById(@PathVariable Long id) {
        return SuccessResponse.of(assembler.toModel(stayService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('STAY_WRITE')")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<EntityModel<Stay>> create(
            @RequestBody Stay stay,
            @RequestParam Long patientId,
            @RequestParam Long roomId,
            @RequestParam Long doctorId) {
        return SuccessResponse.of(assembler.toModel(stayService.create(stay, patientId, roomId, doctorId)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('STAY_WRITE')")
    public SuccessResponse<EntityModel<Stay>> update(
            @PathVariable Long id,
            @RequestBody Stay stay,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) Long doctorId) {
        return SuccessResponse.of(assembler.toModel(stayService.update(id, stay, roomId, doctorId)));
    }

    @PatchMapping("/{id}/discharge")
    @PreAuthorize("hasAuthority('STAY_WRITE')")
    public SuccessResponse<EntityModel<Stay>> discharge(@PathVariable Long id, @RequestBody DischargeRequest req) {
        return SuccessResponse.of(assembler.toModel(stayService.discharge(id, req)));
    }

    @PatchMapping("/{id}/transfer")
    @PreAuthorize("hasAuthority('STAY_WRITE')")
    public SuccessResponse<EntityModel<Stay>> transfer(@PathVariable Long id, @RequestBody TransferRequest req) {
        return SuccessResponse.of(assembler.toModel(stayService.transfer(id, req)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('STAY_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        stayService.delete(id);
    }
}
