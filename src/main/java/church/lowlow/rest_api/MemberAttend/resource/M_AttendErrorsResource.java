package church.lowlow.rest_api.MemberAttend.resource;

import church.lowlow.rest_api.MemberAttend.controller.M_AttendController;
import church.lowlow.rest_api.member.controller.MemberController;
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