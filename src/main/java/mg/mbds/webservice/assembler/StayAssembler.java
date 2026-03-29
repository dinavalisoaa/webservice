package mg.mbds.webservice.assembler;

import mg.mbds.webservice.controller.StayController;
import mg.mbds.webservice.model.Stay;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StayAssembler implements RepresentationModelAssembler<Stay, EntityModel<Stay>> {

    @Override
    public EntityModel<Stay> toModel(Stay stay) {
        EntityModel<Stay> model = EntityModel.of(stay,
                linkTo(methodOn(StayController.class).getById(stay.getId())).withSelfRel(),
                linkTo(methodOn(StayController.class).getAll()).withRel("stays"),
                Link.of("/api/patients/" + stay.getPatient().getId()).withRel("patient"));

        if (stay.getEndDate() == null) {
            model.add(Link.of("/api/stays/" + stay.getId() + "/discharge").withRel("discharge"));
            model.add(Link.of("/api/stays/" + stay.getId() + "/transfer").withRel("transfer"));
        }

        return model;
    }
}
