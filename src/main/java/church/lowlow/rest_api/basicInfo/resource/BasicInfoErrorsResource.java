package church.lowlow.rest_api.basicInfo.resource;

import church.lowlow.rest_api.basicInfo.controller.BasicInfoController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class BasicInfoErrorsResource extends Resource<Errors> {
    public BasicInfoErrorsResource(Errors errors, Link... links) {
        super(errors, links);
        add(linkTo(BasicInfoController.class).withRel("index"));
    }
}