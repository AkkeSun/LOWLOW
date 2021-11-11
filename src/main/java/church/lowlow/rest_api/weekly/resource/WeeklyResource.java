package church.lowlow.rest_api.weekly.resource;

import church.lowlow.rest_api.weekly.controller.WeeklyController;
import church.lowlow.rest_api.weekly.db.Weekly;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class WeeklyResource extends Resource<Weekly> {

    public WeeklyResource(Weekly weekly, Link... links) {
        super(weekly, links);
        add(linkTo(WeeklyController.class).slash(weekly.getId()).withSelfRel()); // _self
    }
}
