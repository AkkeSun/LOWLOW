package church.lowlow.rest_api.notice.controller;

import church.lowlow.rest_api.common.aop.LogComponent;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.gallery.db.Gallery;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.notice.db.Notice;
import church.lowlow.rest_api.notice.db.NoticeDto;
import church.lowlow.rest_api.notice.repository.NoticeRepository;
import church.lowlow.rest_api.notice.resource.NoticeErrorsResource;
import church.lowlow.rest_api.notice.resource.NoticeResource;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private LogComponent logComponent;

    /**
     * CREATE API
     */
    @PostMapping
    @ApiOperation(value = "공지사항 등록", notes = "공지사항을 등록합니다", response = Notice.class)
    public ResponseEntity createNotice(@RequestBody @Valid NoticeDto dto, Errors errors){

        // request param logging
        logComponent.noticeDtoLogging(dto);

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
    @ApiOperation(value = "공지사항 리스트", notes = "공지사항 리스트를 출력합니다", response = Notice.class)
    public ResponseEntity getNotices(PagedResourcesAssembler<Notice> assembler, SearchDto searchDto, PagingDto pagingDto){


        // request param logging
        logComponent.searchDtoLogging(searchDto);
        logComponent.pagingDtoLogging(pagingDto);

        Page<Notice> page = repository.getNoticePage(searchDto, pagingDto);
        var pagedResources = assembler.toResource(page, e -> new NoticeResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/list")
    @ApiIgnore // swagger Ignore
    public ResponseEntity getNoticeList(){

        List<Notice> list = repository.findAll();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("noticeList", list);

        return ResponseEntity.ok().body(resultMap);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "공지사항", notes = "공지사항을 출력합니다", response = Notice.class)
    public ResponseEntity getNotice(@PathVariable Integer id){

        Optional<Notice> optional = repository.findById(id);
        Notice notice = optional.orElseThrow(ArithmeticException::new);

        NoticeResource resource = new NoticeResource(notice);
        return ResponseEntity.ok(resource);
    }



    /**
     * UPDATE API
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "공지사항 수정", notes = "공지사항을 수정합니다", response = Notice.class)
    public ResponseEntity updateNotice(@RequestBody @Valid NoticeDto dto, @PathVariable Integer id, Errors errors){

        // request param logging
        logComponent.noticeDtoLogging(dto);

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
    @ApiOperation(value = "공지사항 삭제", notes = "공지사항을 삭제합니다", response = Notice.class)
    public ResponseEntity deleteNotice(@PathVariable Integer id, Resource resource){

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
