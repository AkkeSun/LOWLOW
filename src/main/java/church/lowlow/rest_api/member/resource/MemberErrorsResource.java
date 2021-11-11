package church.lowlow.rest_api.member.resource;

import church.lowlow.rest_api.member.controller.MemberController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class MemberErrorsResource extends Resource<Errors> {
    public MemberErrorsResource(Errors errors, Link... links) {
        super(errors, links);
        add(linkTo(MemberController.class).withRel("index"));
    }
}