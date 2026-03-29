package mg.mbds.webservice.assembler;

import mg.mbds.webservice.controller.MedicationController;
import mg.mbds.webservice.model.Medication;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MedicationAssembler implements RepresentationModelAssembler<Medication, EntityModel<Medication>> {

    @Override
    public EntityModel<Medication> toModel(Medication medication) {
        return EntityModel.of(medication,
                linkTo(methodOn(MedicationController.class).getById(medication.getId())).withSelfRel(),
                linkTo(methodOn(MedicationController.class).getAll()).withRel("medications"),
                Link.of("/api/medications/" + medication.getId() + "/restock").withRel("restock"),
                linkTo(methodOn(MedicationController.class).getStockAlerts()).withRel("stock-alerts"));
    }
}
