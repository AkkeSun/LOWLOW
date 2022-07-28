package church.lowlow.jwt.resource;

import church.lowlow.jwt.controller.UserController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class UserErrorsResource extends Resource<Errors> {
    public UserErrorsResource(Errors errors, Link... links) {
        super(errors, links);
        add(linkTo(UserController.class).withRel("index"));
    }
}