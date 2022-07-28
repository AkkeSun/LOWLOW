package church.lowlow.jwt.resource;

import church.lowlow.jwt.controller.TokenController;
import church.lowlow.jwt.entity.TokenDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class TokenResource extends Resource<TokenDto> {

    public TokenResource(TokenDto token, Link... links) {
        super(token, links);
        add(linkTo(TokenController.class).slash("issueAccessToken").withSelfRel()); // _self
    }
}
