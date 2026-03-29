package mg.mbds.webservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import mg.mbds.webservice.dto.RoomStatusDTO;
import mg.mbds.webservice.enums.RoomType;
import mg.mbds.webservice.model.Patient;
import mg.mbds.webservice.model.Room;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Lister les chambres disponibles",
               description = "Retourne les chambres non en maintenance dont la capacité n'est pas atteinte à la date donnée (par défaut aujourd'hui)")
    @PreAuthorize("hasAuthority('ROOM_READ')")
    @GetMapping("/available")
    public SuccessResponse<List<Room>> getAvailableRooms(
            @Parameter(description = "Type de chambre") @RequestParam(required = false) RoomType type,
            @Parameter(description = "Capacité minimale souhaitée") @RequestParam(required = false) Integer minCapacity,
            @Parameter(description = "Prix minimum par nuit (inclusif)") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Prix maximum par nuit (inclusif)") @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Date de disponibilité (format ISO : yyyy-MM-dd). Par défaut : date courante.",
                       schema = @Schema(type = "string", format = "date", example = "2026-04-01"))
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return SuccessResponse.of(roomService.getAvailableRooms(type, minCapacity, minPrice, maxPrice, date));
    }

    @Operation(summary = "Lister les patients actuellement présents dans une chambre",
               description = "Retourne les patients dont le séjour dans la chambre n'a pas encore de date de fin (endDate est null)")
    @PreAuthorize("hasAuthority('ROOM_READ')")
    @GetMapping("/{id}/patients")
    public SuccessResponse<List<Patient>> getCurrentPatients(
            @Parameter(description = "Identifiant de la chambre") @PathVariable Long id
    ) {
        return SuccessResponse.of(roomService.getCurrentPatientsInRoom(id));
    }

    @PreAuthorize("hasAuthority('ROOM_WRITE')")
    @PatchMapping("/{id}/maintenance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setMaintenance(@PathVariable Long id, @RequestBody java.util.Map<String, Boolean> body) {
        roomService.setMaintenance(id, body.get("enable"));
    }

    @Operation(summary = "État courant de toutes les chambres",
               description = "Retourne l'occupation et la disponibilité de chaque chambre")
    @PreAuthorize("hasAuthority('ROOM_READ')")
    @GetMapping("/status")
    public ResponseEntity<CollectionModel<EntityModel<RoomStatusDTO>>> getRoomStatuses() {
        List<EntityModel<RoomStatusDTO>> statuses = roomService.getAllRoomStatuses()
                .stream()
                .map(dto -> EntityModel.of(dto,
                        Link.of("/api/rooms/" + dto.getId() + "/patients").withRel("patients"),
                        linkTo(methodOn(RoomController.class).getRoomStatuses()).withSelfRel()))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(statuses,
                linkTo(methodOn(RoomController.class).getRoomStatuses()).withSelfRel()));
    }
}
