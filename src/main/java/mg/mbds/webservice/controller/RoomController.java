package mg.mbds.webservice.controller;

import mg.mbds.webservice.model.Room;
import mg.mbds.webservice.service.RoomService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/available")
    public ResponseEntity<CollectionModel<EntityModel<Room>>> getAvailableRooms() {
        List<EntityModel<Room>> rooms = roomService.getAvailableRooms().stream()
                .map(room -> EntityModel.of(room,
                        linkTo(methodOn(RoomController.class).getAvailableRooms())
                                .slash(room.getId())
                                .withSelfRel()))
                .toList();

        Link selfLink = linkTo(methodOn(RoomController.class).getAvailableRooms()).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(rooms, selfLink));
    }
}
