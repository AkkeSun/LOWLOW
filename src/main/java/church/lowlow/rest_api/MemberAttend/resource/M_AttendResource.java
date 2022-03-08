package church.lowlow.rest_api.MemberAttend.resource;

import church.lowlow.rest_api.MemberAttend.controller.M_AttendController;
import church.lowlow.rest_api.MemberAttend.db.MemberAttend;
import church.lowlow.rest_api.member.controller.MemberController;
import church.lowlow.rest_api.member.db.Member;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class M_AttendResource extends Resource<MemberAttend> {

    public M_AttendResource(MemberAttend memberAttend, Link... links) {
        super(memberAttend, links);
        add(linkTo(M_AttendController.class).slash(memberAttend.getId()).withSelfRel()); // _self
    }
}
