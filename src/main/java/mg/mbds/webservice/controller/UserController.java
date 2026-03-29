package mg.mbds.webservice.controller;

import mg.mbds.webservice.assembler.UserAssembler;
import mg.mbds.webservice.dto.AssignRoleDTO;
import mg.mbds.webservice.dto.DoctorWorkloadDTO;
import mg.mbds.webservice.model.User;
import mg.mbds.webservice.responses.SuccessResponse;
import mg.mbds.webservice.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserAssembler assembler;

    public UserController(UserService userService, UserAssembler assembler) {
        this.userService = userService;
        this.assembler   = assembler;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public SuccessResponse<CollectionModel<EntityModel<User>>> getAll() {
        var users = userService.getAll().stream().map(assembler::toModel).toList();
        return SuccessResponse.of(CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public SuccessResponse<EntityModel<User>> getById(@PathVariable Long id) {
        return SuccessResponse.of(assembler.toModel(userService.getById(id)));
    }

    @GetMapping("/workload")
    @PreAuthorize("hasAuthority('USER_READ')")
    public SuccessResponse<CollectionModel<EntityModel<DoctorWorkloadDTO>>> getDoctorWorkloads() {
        var workloads = userService.getDoctorWorkloads().stream()
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(UserController.class).getById(dto.getId())).withRel("doctor")))
                .toList();
        return SuccessResponse.of(CollectionModel.of(workloads,
                linkTo(methodOn(UserController.class).getDoctorWorkloads()).withSelfRel()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public SuccessResponse<EntityModel<User>> update(@PathVariable Long id, @RequestBody User user) {
        return SuccessResponse.of(assembler.toModel(userService.update(id, user)));
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasAuthority('USER_ASSIGN_ROLE')")
    public SuccessResponse<EntityModel<User>> assignRole(@PathVariable Long id, @RequestBody AssignRoleDTO dto) {
        return SuccessResponse.of(assembler.toModel(userService.assignRole(id, dto.getRole())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
