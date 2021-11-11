package church.lowlow.rest_api.notice.resource;

import church.lowlow.rest_api.notice.controller.NoticeController;
import church.lowlow.rest_api.notice.db.Notice;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class NoticeResource extends Resource<Notice> {

    public NoticeResource(Notice notice, Link... links) {
        super(notice, links);
        add(linkTo(NoticeController.class).slash(notice.getId()).withSelfRel()); // _self
    }
}
