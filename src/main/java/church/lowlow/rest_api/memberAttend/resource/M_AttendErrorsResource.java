package church.lowlow.rest_api.memberAttend.resource;

import church.lowlow.rest_api.memberAttend.controller.M_AttendController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class M_AttendErrorsResource extends Resource<Errors> {
    public M_AttendErrorsResource(Errors errors, Link... links) {
        super(errors, links);
        add(linkTo(M_AttendController.class).withRel("index"));
    }
}