package mg.mbds.webservice.assembler;

import mg.mbds.webservice.controller.UserController;
import mg.mbds.webservice.model.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getById(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAll()).withRel("users"),
                linkTo(methodOn(UserController.class).getDoctorWorkloads()).withRel("workload"));
    }
}
