package church.lowlow.rest_api.notice.controller;

import church.lowlow.rest_api.notice.db.Notice;
import church.lowlow.rest_api.notice.db.NoticeDto;
import church.lowlow.rest_api.notice.repository.NoticeRepository;
import church.lowlow.rest_api.notice.resource.NoticeErrorsResource;
import church.lowlow.rest_api.notice.resource.NoticeResource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static church.lowlow.rest_api.common.util.WriterUtil.getWriter;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping(value = "/api/notices", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class NoticeController {

    @Autowired
    private NoticeRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * CREATE API
     */
    @PostMapping
    public ResponseEntity createWorshipVideo(@RequestBody @Valid NoticeDto dto,
                                              Errors errors){
        // check
        if(errors.hasErrors())
            return badRequest().body(new NoticeErrorsResource(errors));

        // save
        Notice notice = modelMapper.map(dto, Notice.class);
        notice.setWriter(getWriter());
        Notice newNotice = repository.save(notice);
        URI createdUri = linkTo(NoticeController.class).slash(newNotice.getId()).toUri();

        // return
        NoticeResource resource = new NoticeResource(newNotice);
        resource.add(linkTo(NoticeController.class).slash(newNotice.getId()).withRel("update-worshipVideo"));
        resource.add(linkTo(NoticeController.class).slash(newNotice.getId()).withRel("delete-worshipVideo"));

        return ResponseEntity.created(createdUri).body(resource);
    }



    /**
     * READ API
     */
    @GetMapping
    public ResponseEntity getWorshipVideo(Pageable pageable,
                                          PagedResourcesAssembler<Notice> assembler){
        Page<Notice> page = repository.findAll(pageable);
        var pagedResources = assembler.toResource(page, e -> new NoticeResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity geWorshipVideo(@PathVariable Integer id){
        Optional<Notice> optional = repository.findById(id);
        Notice notice = optional.orElseThrow(ArithmeticException::new);

        // 로그인 유무 체크 후 로그인 했으면 update, delete url 넣어주기
        NoticeResource resource = new NoticeResource(notice);
        return ResponseEntity.ok(resource);
    }



    /**
     * UPDATE API
     */
    @PutMapping("/{id}")
    public ResponseEntity updateWorshipVideo(@RequestBody @Valid NoticeDto dto,
                                             @PathVariable Integer id,
                                             Errors errors){

        // check
        Optional<Notice> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();
        if(errors.hasErrors())
            return badRequest().body(new NoticeErrorsResource(errors));

        // save
        Notice notice = modelMapper.map(dto, Notice.class);
        notice.setWriter(getWriter());
        notice.setId(id);
        Notice updateWorshipVideo = repository.save(notice);

        // return
        NoticeResource resource = new NoticeResource(updateWorshipVideo);
        resource.add(linkTo(NoticeController.class).slash(updateWorshipVideo.getId()).withRel("delete-worshipVideo"));

        return ResponseEntity.ok(resource);
    }



    /**
     * DELETE API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteWorshipVideo(@PathVariable Integer id, Resource resource){

        // check
        Optional<Notice> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

        // delete
        repository.deleteById(id);

        // return
        resource.add(linkTo(NoticeController.class).withRel("index"));
        return ResponseEntity.ok(resource);
    }
}
