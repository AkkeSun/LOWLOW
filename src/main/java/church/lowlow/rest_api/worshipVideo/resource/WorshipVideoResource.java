package church.lowlow.rest_api.worshipVideo.resource;

import church.lowlow.rest_api.weekly.controller.WeeklyController;
import church.lowlow.rest_api.weekly.db.Weekly;
import church.lowlow.rest_api.worshipVideo.controller.WorshipVideoController;
import church.lowlow.rest_api.worshipVideo.db.WorshipVideo;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class WorshipVideoResource extends Resource<WorshipVideo> {

    public WorshipVideoResource(WorshipVideo worshipVideo, Link... links) {
        super(worshipVideo, links);
        add(linkTo(WorshipVideoController.class).slash(worshipVideo.getId()).withSelfRel()); // _self
    }
}
