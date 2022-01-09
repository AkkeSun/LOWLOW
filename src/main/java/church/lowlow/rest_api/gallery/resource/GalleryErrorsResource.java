package church.lowlow.rest_api.gallery.resource;

import church.lowlow.rest_api.gallery.controller.GalleryController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class GalleryErrorsResource extends Resource<Errors> {
    public GalleryErrorsResource(Errors errors, Link... links) {
        super(errors, links);
        add(linkTo(GalleryController.class).withRel("index"));
    }
}