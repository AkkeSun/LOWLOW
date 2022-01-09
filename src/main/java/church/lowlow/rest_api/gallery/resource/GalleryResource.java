package church.lowlow.rest_api.gallery.resource;

import church.lowlow.rest_api.gallery.controller.GalleryController;
import church.lowlow.rest_api.gallery.db.Gallery;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class GalleryResource extends Resource<Gallery> {

    public GalleryResource(Gallery worshipVideo, Link... links) {
        super(worshipVideo, links);
        add(linkTo(GalleryController.class).slash(worshipVideo.getId()).withSelfRel()); // _self
    }
}
