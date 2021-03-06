package church.lowlow.rest_api.gallery.controller;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.common.aop.LogComponent;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.gallery.db.Gallery;
import church.lowlow.rest_api.gallery.db.GalleryDto;
import church.lowlow.rest_api.gallery.db.GalleryValidation;
import church.lowlow.rest_api.gallery.repository.GalleryRepository;
import church.lowlow.rest_api.gallery.resource.GalleryErrorsResource;
import church.lowlow.rest_api.gallery.resource.GalleryResource;
import church.lowlow.rest_api.weekly.db.WeeklyDto;
import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private LogComponent logComponent;

    /**
     * CREATE API
     */
    @PostMapping
    @ApiOperation(value = "????????? ??????", notes = "???????????? ???????????????", response = Gallery.class)
    public ResponseEntity createGallery(@RequestBody GalleryDto dto, Errors errors){

        // request param logging
        logComponent.galleryDtoLogging(dto);

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
    @ApiOperation(value = "????????? ?????????", notes = "????????? ???????????? ???????????????", response = Gallery.class)
    public ResponseEntity getGalleries(PagedResourcesAssembler<Gallery> assembler, SearchDto searchDto, PagingDto pagingDto){

        // request param logging
        logComponent.searchDtoLogging(searchDto);
        logComponent.pagingDtoLogging(pagingDto);

        Page<Gallery> page = repository.getGalleryPage(searchDto, pagingDto);
        var pagedResources = assembler.toResource(page, e -> new GalleryResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/list")
    @ApiIgnore // swagger hide
    public ResponseEntity getGalleryList(){

        List<Gallery> list = repository.findAll();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("galleryList", list);

        return ResponseEntity.ok().body(resultMap);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "?????????", notes = "??? ?????? ???????????? ???????????????", response = Gallery.class)
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
    @ApiOperation(value = "????????? ??????", notes = "???????????? ???????????????", response = Gallery.class)
    public ResponseEntity updateGallery(@RequestBody GalleryDto dto, @PathVariable Integer id, Errors errors){

        // request param logging
        logComponent.galleryDtoLogging(dto);

        // check
        galleryValidation.validate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new GalleryErrorsResource(errors));

        // update
        Gallery gallery = modelMapper.map(dto, Gallery.class);
        gallery.setId(id);
        gallery.setWriter(getWriter());
        Gallery updateGallery = repository.save(gallery);

        // return
        GalleryResource resource = new GalleryResource(updateGallery);
        resource.add(linkTo(GalleryController.class).slash(updateGallery.getId()).withRel("delete-worshipVideo"));

        return ResponseEntity.ok(resource);
    }



    /**
     * DELETE API
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "????????? ??????", notes = "???????????? ???????????????", response = Gallery.class)
    public ResponseEntity deleteGallery(@PathVariable Integer id, Resource resource){

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
