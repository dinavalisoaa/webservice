package mg.mbds.webservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import mg.mbds.webservice.enums.RoomType;
import mg.mbds.webservice.dto.RoomStatusDTO;
import mg.mbds.webservice.model.Patient;
import mg.mbds.webservice.model.Room;
import mg.mbds.webservice.service.RoomService;
import mg.mbds.webservice.service.RoomSseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final RoomSseService roomSseService;

    public RoomController(RoomService roomService, RoomSseService roomSseService) {
        this.roomService = roomService;
        this.roomSseService = roomSseService;
    }

    @Operation(summary = "Lister les chambres disponibles",
               description = "Retourne les chambres non en maintenance dont la capacité n'est pas atteinte à la date donnée (par défaut aujourd'hui)")
    @PreAuthorize("hasAuthority('ROOM_READ')")
    @GetMapping("/available")
    public ResponseEntity<CollectionModel<EntityModel<Room>>> getAvailableRooms(
            @Parameter(description = "Type de chambre") @RequestParam(required = false) RoomType type,
            @Parameter(description = "Capacité minimale souhaitée") @RequestParam(required = false) Integer minCapacity,
            @Parameter(description = "Prix minimum par nuit (inclusif)") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Prix maximum par nuit (inclusif)") @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Date de disponibilité (format ISO : yyyy-MM-dd). Par défaut : date courante.",
                       schema = @Schema(type = "string", format = "date", example = "2026-04-01"))
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<EntityModel<Room>> rooms = roomService
                .getAvailableRooms(type, minCapacity, minPrice, maxPrice, date)
                .stream()
                .map(room -> EntityModel.of(room,
                        linkTo(methodOn(RoomController.class)
                                .getAvailableRooms(null, null, null, null, null))
                                .slash(room.getId())
                                .withSelfRel()))
                .toList();

        Link selfLink = linkTo(methodOn(RoomController.class)
                .getAvailableRooms(type, minCapacity, minPrice, maxPrice, date))
                .withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(rooms, selfLink));
    }

    @Operation(summary = "Lister les patients actuellement présents dans une chambre",
               description = "Retourne les patients dont le séjour dans la chambre n'a pas encore de date de fin (endDate est null)")
    @PreAuthorize("hasAuthority('ROOM_READ')")
    @GetMapping("/{id}/patients")
    public ResponseEntity<CollectionModel<EntityModel<Patient>>> getCurrentPatients(
            @Parameter(description = "Identifiant de la chambre") @PathVariable Long id
    ) {
        List<EntityModel<Patient>> patients = roomService.getCurrentPatientsInRoom(id)
                .stream()
                .map(patient -> EntityModel.of(patient,
                        Link.of("/api/patients/" + patient.getId()).withSelfRel(),
                        linkTo(methodOn(RoomController.class).getCurrentPatients(id)).withRel("room-patients")))
                .toList();

        Link selfLink = linkTo(methodOn(RoomController.class).getCurrentPatients(id)).withSelfRel();
        Link roomLink = linkTo(methodOn(RoomController.class)
                .getAvailableRooms(null, null, null, null, null))
                .slash(id)
                .withRel("room");

        return ResponseEntity.ok(CollectionModel.of(patients, selfLink, roomLink));
    }

    @Operation(summary = "État en temps réel de toutes les chambres",
               description = "Retourne l'état courant de toutes les chambres, y compris les chambres vides")
    @PreAuthorize("hasAuthority('ROOM_READ')")
    @GetMapping("/status")
    public ResponseEntity<CollectionModel<EntityModel<RoomStatusDTO>>> getRoomStatuses() {
        List<EntityModel<RoomStatusDTO>> statuses = roomService.getAllRoomStatuses()
                .stream()
                .map(dto -> EntityModel.of(dto,
                        Link.of("/api/rooms/" + dto.getId() + "/patients").withRel("patients"),
                        linkTo(methodOn(RoomController.class).getRoomStatuses()).withSelfRel()))
                .toList();

        Link selfLink = linkTo(methodOn(RoomController.class).getRoomStatuses()).withSelfRel();
        Link streamLink = Link.of("/api/rooms/status/stream").withRel("stream");

        return ResponseEntity.ok(CollectionModel.of(statuses, selfLink, streamLink));
    }

    @PreAuthorize("hasAuthority('ROOM_READ')")
    @GetMapping(value = "/status/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamRoomStatuses() {
        return roomSseService.subscribe();
    }
}
