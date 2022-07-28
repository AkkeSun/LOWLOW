package church.lowlow.jwt.resource;

import church.lowlow.jwt.controller.ExceptionController;
import church.lowlow.jwt.entity.TokenDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class TokenErrorsResource extends Resource<TokenDto> {
    public TokenErrorsResource(TokenDto token, Link... links) {
        super(token, links);
        add(linkTo(ExceptionController.class).slash(token.getErrCode()).withSelfRel());
    }
}