package church.lowlow.rest_api.gallery.controller;

import church.lowlow.rest_api.gallery.db.Gallery;
import church.lowlow.rest_api.gallery.db.GalleryDto;
import church.lowlow.rest_api.gallery.repository.GalleryRepository;
import church.lowlow.rest_api.gallery.resource.GalleryErrorsResource;
import church.lowlow.rest_api.gallery.resource.GalleryResource;
import church.lowlow.rest_api.weekly.db.WeeklyDto;
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
@RequestMapping(value = "/api/galleries", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class GalleryController {

    @Autowired
    private GalleryRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * CREATE API
     */
    @PostMapping
    public ResponseEntity createWorshipVideo(@RequestBody @Valid GalleryDto dto,
                                              Errors errors){
        // check
        if(errors.hasErrors())
            return badRequest().body(new GalleryErrorsResource(errors));

        // save
        Gallery worshipVideo = modelMapper.map(dto, Gallery.class);
        Gallery newWorshipVideo = repository.save(worshipVideo);
        URI createdUri = linkTo(GalleryController.class).slash(newWorshipVideo.getId()).toUri();

        // return
        GalleryResource resource = new GalleryResource(newWorshipVideo);
        resource.add(linkTo(GalleryController.class).slash(newWorshipVideo.getId()).withRel("update-worshipVideo"));
        resource.add(linkTo(GalleryController.class).slash(newWorshipVideo.getId()).withRel("delete-worshipVideo"));

        return ResponseEntity.created(createdUri).body(resource);
    }



    /**
     * READ API
     */
    @GetMapping
    public ResponseEntity getWorshipVideo(Pageable pageable,
                                          PagedResourcesAssembler<Gallery> assembler){
        Page<Gallery> page = repository.findAll(pageable);
        var pagedResources = assembler.toResource(page, e -> new GalleryResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity geWorshipVideo(@PathVariable Integer id){
        Optional<Gallery> optional = repository.findById(id);
        Gallery worshipVideo = optional.orElseThrow(ArithmeticException::new);

        // 로그인 유무 체크 후 로그인 했으면 update, delete url 넣어주기
        GalleryResource resource = new GalleryResource(worshipVideo);
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
        Optional<Gallery> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();
        if(errors.hasErrors())
            return badRequest().body(new GalleryErrorsResource(errors));

        // save
        Gallery worshipVideo = modelMapper.map(dto, Gallery.class);
        worshipVideo.setId(id);
        Gallery updateWorshipVideo = repository.save(worshipVideo);

        // return
        GalleryResource resource = new GalleryResource(updateWorshipVideo);
        resource.add(linkTo(GalleryController.class).slash(updateWorshipVideo.getId()).withRel("delete-worshipVideo"));

        return ResponseEntity.ok(resource);
    }



    /**
     * DELETE API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteWorshipVideo(@PathVariable Integer id, Resource resource){

        // check
        Optional<Gallery> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

        // delete
        repository.deleteById(id);

        // return
        resource.add(linkTo(GalleryController.class).withRel("index"));
        return ResponseEntity.ok(resource);
    }
}
