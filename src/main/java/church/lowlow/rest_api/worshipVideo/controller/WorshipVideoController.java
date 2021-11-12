package church.lowlow.rest_api.worshipVideo.controller;

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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping(value = "/api/worshipVideos", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class WorshipVideoController {

    @Autowired
    private WorshipRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * CREATE API
     */
    @PostMapping
    public ResponseEntity createWorshipVideo(@RequestBody @Valid WorshipVideoDto dto,
                                              Errors errors){
        // check
        if(errors.hasErrors())
            return badRequest().body(new WorshipVideoErrorsResource(errors));

        // save
        WorshipVideo worshipVideo = modelMapper.map(dto, WorshipVideo.class);
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
    public ResponseEntity getWorshipVideo(Pageable pageable,
                                         PagedResourcesAssembler<WorshipVideo> assembler){
        Page<WorshipVideo> page = repository.findAll(pageable);
        var pagedResources = assembler.toResource(page, e -> new WorshipVideoResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity getWorshipVideo(@PathVariable Integer id){
        Optional<WorshipVideo> optional = repository.findById(id);
        WorshipVideo worshipVideo = optional.orElseThrow(ArithmeticException::new);

        // 로그인 유무 체크 후 로그인 했으면 update, delete url 넣어주기
        WorshipVideoResource resource = new WorshipVideoResource(worshipVideo);
        return ResponseEntity.ok(resource);
    }



    /**
     * UPDATE API
     */
    @PutMapping("/{id}")
    public ResponseEntity updateWorshipVideo(@RequestBody @Valid WeeklyDto dto,
                                             @PathVariable Integer id,
                                             Errors errors){

        // check
        Optional<WorshipVideo> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();
        if(errors.hasErrors())
            return badRequest().body(new WorshipVideoErrorsResource(errors));

        // save
        WorshipVideo worshipVideo = modelMapper.map(dto, WorshipVideo.class);
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
