package church.lowlow.rest_api.calendar.resource;

import church.lowlow.rest_api.calendar.controller.CalenderController;
import church.lowlow.rest_api.calendar.db.Calendar;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class CalendarResource extends Resource<Calendar> {

    public CalendarResource(Calendar notice, Link... links) {
        super(notice, links);
        add(linkTo(CalenderController.class).slash(notice.getId()).withSelfRel()); // _self
    }
}
