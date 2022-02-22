package church.lowlow.rest_api.summerNote.resource;

import church.lowlow.rest_api.summerNote.controller.SummerNoteImgController;
import church.lowlow.rest_api.summerNote.db.SummerNoteImg;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class SummernoteImgResource extends Resource<SummerNoteImg> {

    public SummernoteImgResource(SummerNoteImg summernote, Link... links) {
        super(summernote, links);
        add(linkTo(SummerNoteImgController.class).slash(summernote.getId()).withSelfRel()); // _self
    }
}
