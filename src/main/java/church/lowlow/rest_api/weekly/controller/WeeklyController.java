package church.lowlow.rest_api.weekly.controller;

import church.lowlow.rest_api.common.aop.LogComponent;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.weekly.db.Weekly;
import church.lowlow.rest_api.weekly.db.WeeklyDto;
import church.lowlow.rest_api.weekly.db.WeeklyValidation;
import church.lowlow.rest_api.weekly.repository.WeeklyRepository;
import church.lowlow.rest_api.weekly.resource.WeeklyErrorsResource;
import church.lowlow.rest_api.weekly.resource.WeeklyResource;
import church.lowlow.user_api.common.fileProcess.service.basic.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static church.lowlow.rest_api.common.util.WriterUtil.getWriter;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping(value = "/api/weekly", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class WeeklyController {

    @Autowired
    private WeeklyRepository repository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LogComponent logComponent;

    @Autowired
    private WeeklyValidation validation;

    /**
     * CREATE API
     */
    @PostMapping
    public ResponseEntity createWeekly(@RequestBody WeeklyDto dto, Errors errors){

        // request param logging
        logComponent.weeklyDtoLogging(dto);

        // check
        validation.validate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new WeeklyErrorsResource(errors));

        // save
        Weekly weekly = modelMapper.map(dto, Weekly.class);
        weekly.setWriter(getWriter());
        Weekly newWeekly = repository.save(weekly);
        URI createdUri = linkTo(WeeklyController.class).slash(newWeekly.getId()).toUri();

        // return
        WeeklyResource resource = new WeeklyResource(newWeekly);
        resource.add(linkTo(WeeklyController.class).slash(newWeekly.getId()).withRel("update-weekly"));
        resource.add(linkTo(WeeklyController.class).slash(newWeekly.getId()).withRel("delete-weekly"));

        return ResponseEntity.created(createdUri).body(resource);
    }



    /**
     * READ API
     */
    @GetMapping
    public ResponseEntity getWeekly(SearchDto searchDto, PagingDto pagingDto, PagedResourcesAssembler<Weekly> assembler){
        Page<Weekly> page = repository.getWeeklyList(searchDto, pagingDto);
        var pagedResources = assembler.toResource(page, e -> new WeeklyResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getWeekly(@PathVariable Integer id){

        Optional<Weekly> optional = repository.findById(id);
        Weekly weekly = optional.orElseThrow(ArithmeticException::new);

        WeeklyResource resource = new WeeklyResource(weekly);
        return ResponseEntity.ok(resource);
    }



    /**
     * UPDATE API
     */
    @PutMapping("/{id}")
    public ResponseEntity updateWeekly(@RequestBody WeeklyDto dto, @PathVariable Integer id, Errors errors){

        // request param logging
        logComponent.weeklyDtoLogging(dto);

        // check
        Optional<Weekly> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();
        validation.validate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new WeeklyErrorsResource(errors));

        // save
        Weekly weekly = modelMapper.map(dto, Weekly.class);
        weekly.setWriter(getWriter());
        weekly.setId(id);
        Weekly updateWeekly = repository.save(weekly);

        // return
        WeeklyResource resource = new WeeklyResource(updateWeekly);
        resource.add(linkTo(WeeklyController.class).slash(updateWeekly.getId()).withRel("delete-weekly"));

        return ResponseEntity.ok(resource);
    }



    /**
     * DELETE API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteMembers(@PathVariable Integer id, Resource resource){

        // check
        Optional<Weekly> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

        // delete
        fileDelete(optional.get());
        repository.deleteById(id);

        // return
        resource.add(linkTo(WeeklyController.class).withRel("index"));
        return ResponseEntity.ok(resource);
    }

    public void fileDelete(Weekly weekly){
        if(weekly.getImg1() != null)
            fileService.deleteFile(weekly.getImg1().getUploadName(), "weekly");
        if(weekly.getImg2() != null)
            fileService.deleteFile(weekly.getImg2().getUploadName(), "weekly");
        if(weekly.getImg3() != null)
            fileService.deleteFile(weekly.getImg3().getUploadName(), "weekly");
        if(weekly.getImg4() != null)
            fileService.deleteFile(weekly.getImg4().getUploadName(), "weekly");
    }
}
