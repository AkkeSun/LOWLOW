package church.lowlow.rest_api.member.resource;

import church.lowlow.rest_api.member.controller.MemberController;
import church.lowlow.rest_api.member.db.Member;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class MemberResource  extends Resource<Member> {

    public MemberResource(Member member, Link... links) {
        super(member, links);
        add(linkTo(MemberController.class).slash(member.getId()).withSelfRel()); // _self
    }
}
