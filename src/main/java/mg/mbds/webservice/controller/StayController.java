package mg.mbds.webservice.controller;

import mg.mbds.webservice.model.Stay;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.StayService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stays")
public class StayController {

    private final StayService stayService;

    public StayController(StayService stayService) {
        this.stayService = stayService;
    }

    @GetMapping
    public SuccessResponse<List<Stay>> getAll() {
        return SuccessResponse.of(stayService.getAll());
    }

    @GetMapping("/{id}")
    public SuccessResponse<Stay> getById(@PathVariable Long id) {
        return SuccessResponse.of(stayService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Stay> create(
            @RequestBody Stay stay,
            @RequestParam Long patientId,
            @RequestParam Long roomId,
            @RequestParam Long doctorId) {
        return SuccessResponse.of(stayService.create(stay, patientId, roomId, doctorId));
    }

    @PutMapping("/{id}")
    public SuccessResponse<Stay> update(
            @PathVariable Long id,
            @RequestBody Stay stay,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) Long doctorId) {
        return SuccessResponse.of(stayService.update(id, stay, roomId, doctorId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        stayService.delete(id);
    }
}
