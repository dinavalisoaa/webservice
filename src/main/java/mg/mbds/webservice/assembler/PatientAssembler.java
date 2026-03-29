package mg.mbds.webservice.assembler;

import mg.mbds.webservice.controller.PatientController;
import mg.mbds.webservice.model.Patient;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PatientAssembler implements RepresentationModelAssembler<Patient, EntityModel<Patient>> {

    @Override
    public EntityModel<Patient> toModel(Patient patient) {
        return EntityModel.of(patient,
                linkTo(methodOn(PatientController.class).getById(patient.getId())).withSelfRel(),
                linkTo(methodOn(PatientController.class).getStays(patient.getId())).withRel("stays"),
                linkTo(methodOn(PatientController.class).getAll()).withRel("patients"));
    }
}
