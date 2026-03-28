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
import mg.mbds.webservice.service.sse.RoomSseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.util.List;

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
    @GetMapping("/{id}/patients")
    public SuccessResponse<List<Patient>> getCurrentPatients(
            @Parameter(description = "Identifiant de la chambre") @PathVariable Long id
    ) {
        return SuccessResponse.of(roomService.getCurrentPatientsInRoom(id));
    }

    @Operation(summary = "État en temps réel de toutes les chambres",
               description = "Retourne l'état courant de toutes les chambres, y compris les chambres vides")
    @GetMapping("/status")
    public SuccessResponse<List<RoomStatusDTO>> getRoomStatuses() {
        return SuccessResponse.of(roomService.getAllRoomStatuses());
    }

    @GetMapping(value = "/status/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamRoomStatuses() {
        return roomSseService.subscribe();
    }
}
