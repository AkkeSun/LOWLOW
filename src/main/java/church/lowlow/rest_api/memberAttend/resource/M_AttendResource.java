package church.lowlow.rest_api.memberAttend.resource;

import church.lowlow.rest_api.memberAttend.controller.M_AttendController;
import church.lowlow.rest_api.memberAttend.db.MemberAttend;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class M_AttendResource extends Resource<MemberAttend> {

    public M_AttendResource(MemberAttend memberAttend, Link... links) {
        super(memberAttend, links);
        add(linkTo(M_AttendController.class).slash(memberAttend.getId()).withSelfRel()); // _self
    }
}
