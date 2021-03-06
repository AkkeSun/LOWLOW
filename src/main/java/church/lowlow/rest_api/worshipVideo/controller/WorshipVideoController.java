package church.lowlow.rest_api.worshipVideo.controller;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.common.aop.LogComponent;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.weekly.db.Weekly;
import church.lowlow.rest_api.weekly.db.WeeklyDto;
import church.lowlow.rest_api.weekly.repository.WeeklyRepository;
import church.lowlow.rest_api.weekly.resource.WeeklyErrorsResource;
import church.lowlow.rest_api.weekly.resource.WeeklyResource;
import church.lowlow.rest_api.worshipVideo.db.WorshipVideo;
import church.lowlow.rest_api.worshipVideo.db.WorshipVideoDto;
import church.lowlow.rest_api.worshipVideo.repository.WorshipRepository;
import church.lowlow.rest_api.worshipVideo.resource.WorshipVideoErrorsResource;
import church.lowlow.rest_api.worshipVideo.resource.WorshipVideoResource;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping(value = "/api/worshipVideos", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class WorshipVideoController {

    @Autowired
    private WorshipRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LogComponent logComponent;

    /**
     * CREATE API
     */
    @PostMapping
    @ApiOperation(value = "?????? ?????? ??????", notes = "?????? ????????? ???????????????", response = WorshipVideo.class)
    public ResponseEntity createWorshipVideo(@RequestBody @Valid WorshipVideoDto dto, Errors errors){

        // request param logging
        logComponent.worshipVideoDtoLogging(dto);

        // check
        if(errors.hasErrors())
            return badRequest().body(new WorshipVideoErrorsResource(errors));

        // save
        WorshipVideo worshipVideo = modelMapper.map(dto, WorshipVideo.class);
        worshipVideo.setWriter(getWriter());
        WorshipVideo newWorshipVideo = repository.save(worshipVideo);
        URI createdUri = linkTo(WorshipVideoController.class).slash(newWorshipVideo.getId()).toUri();

        // return
        WorshipVideoResource resource = new WorshipVideoResource(newWorshipVideo);
        resource.add(linkTo(WorshipVideoController.class).slash(newWorshipVideo.getId()).withRel("update-worshipVideo"));
        resource.add(linkTo(WorshipVideoController.class).slash(newWorshipVideo.getId()).withRel("delete-worshipVideo"));

        return ResponseEntity.created(createdUri).body(resource);
    }



    /**
     * READ API
     */
    @GetMapping
    @ApiOperation(value = "?????? ?????? ?????????", notes = "?????? ?????? ???????????? ???????????????", response = WorshipVideo.class)
    public ResponseEntity getWorshipVideo(SearchDto searchDto, PagingDto pagingDto, PagedResourcesAssembler<WorshipVideo> assembler){

        // request param logging
        logComponent.searchDtoLogging(searchDto);
        logComponent.pagingDtoLogging(pagingDto);

        Page<WorshipVideo> page = repository.getWorshipVideList(searchDto, pagingDto);

        var pagedResources = assembler.toResource(page, e -> new WorshipVideoResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "?????? ??????", notes = "??? ?????? ?????? ????????? ???????????????", response = WorshipVideo.class)
    public ResponseEntity getWorshipVideo(@PathVariable Integer id){

        Optional<WorshipVideo> optional = repository.findById(id);
        WorshipVideo worshipVideo = optional.orElseThrow(ArithmeticException::new);

        WorshipVideoResource resource = new WorshipVideoResource(worshipVideo);
        return ResponseEntity.ok(resource);
    }



    /**
     * UPDATE API
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "?????? ?????? ??????", notes = "??? ?????? ?????? ???????????????", response = WorshipVideo.class)
    public ResponseEntity updateWorshipVideo(@RequestBody @Valid WorshipVideoDto dto, @PathVariable Integer id, Errors errors){

        // request param logging
        logComponent.worshipVideoDtoLogging(dto);

        // check
        Optional<WorshipVideo> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();
        if(errors.hasErrors())
            return badRequest().body(new WorshipVideoErrorsResource(errors));

        // save
        WorshipVideo worshipVideo = modelMapper.map(dto, WorshipVideo.class);
        worshipVideo.setWriter(getWriter());
        worshipVideo.setId(id);
        WorshipVideo updateWorshipVideo = repository.save(worshipVideo);

        // return
        WorshipVideoResource resource = new WorshipVideoResource(updateWorshipVideo);
        resource.add(linkTo(WorshipVideoController.class).slash(updateWorshipVideo.getId()).withRel("delete-worshipVideo"));

        return ResponseEntity.ok(resource);
    }



    /**
     * DELETE API
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "?????? ?????? ??????", notes = "?????? ????????? ???????????????", response = WorshipVideo.class)
    public ResponseEntity deleteWorshipVideo(@PathVariable Integer id, Resource resource){

        // check
        Optional<WorshipVideo> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

        // delete
        repository.deleteById(id);

        // return
        resource.add(linkTo(WorshipVideoController.class).withRel("index"));
        return ResponseEntity.ok(resource);
    }
}
