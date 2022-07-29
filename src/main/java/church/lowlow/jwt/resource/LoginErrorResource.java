package church.lowlow.jwt.resource;

import church.lowlow.jwt.controller.LoginController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class LoginErrorResource extends Resource<Errors> {
    public LoginErrorResource(Errors errors, Link... links) {
        super(errors, links);
        add(linkTo(LoginController.class).withSelfRel());
    }
}