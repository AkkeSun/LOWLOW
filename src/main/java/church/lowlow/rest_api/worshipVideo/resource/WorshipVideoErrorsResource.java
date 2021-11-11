package church.lowlow.rest_api.worshipVideo.resource;

import church.lowlow.rest_api.worshipVideo.controller.WorshipVideoController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class WorshipVideoErrorsResource extends Resource<Errors> {
    public WorshipVideoErrorsResource(Errors errors, Link... links) {
        super(errors, links);
        add(linkTo(WorshipVideoController.class).withRel("index"));
    }
}