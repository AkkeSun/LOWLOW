package church.lowlow.rest_api.weekly.resource;

import church.lowlow.rest_api.member.controller.MemberController;
import church.lowlow.rest_api.weekly.controller.WeeklyController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class WeeklyErrorsResource extends Resource<Errors> {
    public WeeklyErrorsResource(Errors errors, Link... links) {
        super(errors, links);
        add(linkTo(WeeklyController.class).withRel("index"));
    }
}