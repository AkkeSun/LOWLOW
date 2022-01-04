package church.lowlow.rest_api.gallery.controller;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.gallery.db.Gallery;
import church.lowlow.rest_api.gallery.db.GalleryDto;
import church.lowlow.rest_api.gallery.db.GalleryValidation;
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

import static church.lowlow.rest_api.common.util.WriterUtil.getWriter;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping(value = "/api/galleries", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class GalleryController {

    @Autowired
    private GalleryRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GalleryValidation galleryValidation;

    /**
     * CREATE API
     */
    @PostMapping
    public ResponseEntity createWorshipVideo(@RequestBody GalleryDto dto, Errors errors){

        // check
        galleryValidation.validate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new GalleryErrorsResource(errors));

        // save
        Gallery gallery = modelMapper.map(dto, Gallery.class);
        gallery.setWriter(getWriter());
        Gallery savedData = repository.save(gallery);
        URI createdUri = linkTo(GalleryController.class).slash(savedData.getId()).toUri();

        // return
        GalleryResource resource = new GalleryResource(savedData);
        resource.add(linkTo(GalleryController.class).slash(savedData.getId()).withRel("update-worshipVideo"));
        resource.add(linkTo(GalleryController.class).slash(savedData.getId()).withRel("delete-worshipVideo"));

        return ResponseEntity.created(createdUri).body(resource);
    }



    /**
     * READ API
     */
    @GetMapping
    public ResponseEntity getGalleries(PagedResourcesAssembler<Gallery> assembler, SearchDto searchDto, PagingDto pagingDto){

        Page<Gallery> page = repository.getGalleryPage(searchDto, pagingDto);
        var pagedResources = assembler.toResource(page, e -> new GalleryResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity getGallery(@PathVariable Integer id){
        Optional<Gallery> optional = repository.findById(id);
        Gallery gallery = optional.orElseThrow(ArithmeticException::new);

        GalleryResource resource = new GalleryResource(gallery);
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
