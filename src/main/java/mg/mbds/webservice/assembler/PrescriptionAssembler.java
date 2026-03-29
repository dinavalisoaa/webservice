package mg.mbds.webservice.assembler;

import mg.mbds.webservice.controller.PrescriptionController;
import mg.mbds.webservice.enums.PrescriptionStatus;
import mg.mbds.webservice.model.Prescription;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PrescriptionAssembler implements RepresentationModelAssembler<Prescription, EntityModel<Prescription>> {

    @Override
    public EntityModel<Prescription> toModel(Prescription prescription) {
        EntityModel<Prescription> model = EntityModel.of(prescription,
                linkTo(methodOn(PrescriptionController.class).getById(prescription.getId())).withSelfRel(),
                linkTo(methodOn(PrescriptionController.class).getMedications(prescription.getId())).withRel("medications"),
                linkTo(methodOn(PrescriptionController.class).getAll()).withRel("prescriptions"),
                Link.of("/api/stays/" + prescription.getStay().getId()).withRel("stay"));

        if (prescription.getStatus() == PrescriptionStatus.ACTIVE) {
            model.add(Link.of("/api/prescriptions/" + prescription.getId() + "/dispense").withRel("dispense"));
            model.add(Link.of("/api/prescriptions/" + prescription.getId() + "/cancel").withRel("cancel"));
        }

        return model;
    }
}
