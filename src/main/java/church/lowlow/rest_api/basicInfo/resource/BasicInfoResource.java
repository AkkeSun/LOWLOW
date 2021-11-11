package church.lowlow.rest_api.basicInfo.resource;

import church.lowlow.rest_api.basicInfo.controller.BasicInfoController;
import church.lowlow.rest_api.basicInfo.db.BasicInfo;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class BasicInfoResource extends Resource<BasicInfo> {

    public BasicInfoResource(BasicInfo info, Link... links) {
        super(info, links);
        add(linkTo(BasicInfoController.class).slash(info.getId()).withSelfRel()); // _self
    }
}
