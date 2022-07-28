package church.lowlow.jwt.resource;

import church.lowlow.jwt.controller.UserController;
import church.lowlow.jwt.entity.UserDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class UserResource extends Resource<UserDto> {

    public UserResource(UserDto user, Link... links) {
        super(user, links);
        add(linkTo(UserController.class).slash(user.getId()).withSelfRel()); // _self
    }
}
