package church.lowlow.rest_api.calendar.controller;

import church.lowlow.rest_api.calendar.db.Calendar;
import church.lowlow.rest_api.calendar.db.CalendarDto;
import church.lowlow.rest_api.calendar.db.CalenderValidation;
import church.lowlow.rest_api.calendar.repository.CalendarRepository;
import church.lowlow.rest_api.calendar.resource.CalendarErrorsResource;
import church.lowlow.rest_api.calendar.resource.CalendarResource;
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
@RequestMapping(value = "/api/calendars", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class CalenderController {

    @Autowired
    private CalendarRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CalenderValidation validation;

    /**
     * CREATE API
     */
    @PostMapping
    public ResponseEntity createWorshipVideo(@RequestBody @Valid CalendarDto dto,
                                              Errors errors){
        // check
        if(errors.hasErrors())
            return badRequest().body(new CalendarErrorsResource(errors));
        validation.validate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new CalendarErrorsResource(errors));

        // save
        Calendar calendar = modelMapper.map(dto, Calendar.class);
        Calendar newCalendar = repository.save(calendar);
        URI createdUri = linkTo(CalenderController.class).slash(newCalendar.getId()).toUri();

        // return
        CalendarResource resource = new CalendarResource(newCalendar);
        resource.add(linkTo(CalenderController.class).slash(newCalendar.getId()).withRel("update-calendar"));
        resource.add(linkTo(CalenderController.class).slash(newCalendar.getId()).withRel("delete-calendar"));

        return ResponseEntity.created(createdUri).body(resource);
    }



    /**
     * READ API
     */
    @GetMapping
    public ResponseEntity getWorshipVideo(Pageable pageable,
                                          PagedResourcesAssembler<Calendar> assembler){
        Page<Calendar> page = repository.findAll(pageable);
        var pagedResources = assembler.toResource(page, e -> new CalendarResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity geWorshipVideo(@PathVariable Integer id){
        Optional<Calendar> optional = repository.findById(id);
        Calendar notice = optional.orElseThrow(ArithmeticException::new);

        // 로그인 유무 체크 후 로그인 했으면 update, delete url 넣어주기
        CalendarResource resource = new CalendarResource(notice);
        return ResponseEntity.ok(resource);
    }



    /**
     * UPDATE API
     */
    @PutMapping("/{id}")
    public ResponseEntity updateWorshipVideo(@RequestBody @Valid CalendarDto dto,
                                             @PathVariable Integer id,
                                             Errors errors){

        // check
        Optional<Calendar> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();
        if(errors.hasErrors())
            return badRequest().body(new CalendarErrorsResource(errors));

        // save
        Calendar notice = modelMapper.map(dto, Calendar.class);
        notice.setId(id);
        Calendar updateWorshipVideo = repository.save(notice);

        // return
        CalendarResource resource = new CalendarResource(updateWorshipVideo);
        resource.add(linkTo(CalenderController.class).slash(updateWorshipVideo.getId()).withRel("delete-worshipVideo"));

        return ResponseEntity.ok(resource);
    }



    /**
     * DELETE API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteWorshipVideo(@PathVariable Integer id, Resource resource){

        // check
        Optional<Calendar> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

        // delete
        repository.deleteById(id);

        // return
        resource.add(linkTo(CalenderController.class).withRel("index"));
        return ResponseEntity.ok(resource);
    }
}
