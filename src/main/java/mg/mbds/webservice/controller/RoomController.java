package mg.mbds.webservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import mg.mbds.webservice.model.Room;
import mg.mbds.webservice.model.RoomType;
import mg.mbds.webservice.service.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Lister les chambres disponibles",
               description = "Retourne les chambres non en maintenance dont la capacité n'est pas atteinte à la date donnée (par défaut aujourd'hui). Tous les filtres sont optionnels.")
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
}
