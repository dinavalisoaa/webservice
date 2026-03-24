package mg.mbds.webservice.service;

import mg.mbds.webservice.dto.RoomStatusDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class RoomSseService {

    private final RoomService roomService;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public RoomSseService(RoomService roomService) {
        this.roomService = roomService;
    }
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        // Envoi immédiat de l'état courant à la connexion
        try {
            emitter.send(SseEmitter.event()
                    .name("room-status")
                    .data(roomService.getAllRoomStatuses()));
        } catch (IOException e) {
            emitters.remove(emitter);
            emitter.completeWithError(e);
        }

        return emitter;
    }

//    Envoie l'état de toutes les chambres à tous les clients toutes les 5 secondes
    @Scheduled(fixedDelay = 5000)
    public void pushUpdates() {
        if (emitters.isEmpty()) {
            return;
        }

        List<RoomStatusDTO> statuses = roomService.getAllRoomStatuses();
        List<SseEmitter> dead = new java.util.ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("room-status")
                        .data(statuses));
            } catch (IOException e) {
                dead.add(emitter);
            }
        }

        emitters.removeAll(dead);
    }
}
