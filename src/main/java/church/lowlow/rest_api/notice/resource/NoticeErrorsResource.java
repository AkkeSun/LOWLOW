package church.lowlow.rest_api.notice.resource;

import church.lowlow.rest_api.notice.controller.NoticeController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class NoticeErrorsResource extends Resource<Errors> {
    public NoticeErrorsResource(Errors errors, Link... links) {
        super(errors, links);
        add(linkTo(NoticeController.class).withRel("index"));
    }
}