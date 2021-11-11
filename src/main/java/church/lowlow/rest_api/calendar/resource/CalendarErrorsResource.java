package church.lowlow.rest_api.calendar.resource;

import church.lowlow.rest_api.calendar.controller.CalenderController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class CalendarErrorsResource extends Resource<Errors> {
    public CalendarErrorsResource(Errors errors, Link... links) {
        super(errors, links);
        add(linkTo(CalenderController.class).withRel("index"));
    }
}