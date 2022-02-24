package church.lowlow.rest_api.calendar.controller;

import church.lowlow.rest_api.calendar.db.Calendar;
import church.lowlow.rest_api.calendar.db.CalendarDto;
import church.lowlow.rest_api.calendar.db.CalenderValidation;
import church.lowlow.rest_api.calendar.repository.CalendarRepository;
import church.lowlow.rest_api.calendar.resource.CalendarErrorsResource;
import church.lowlow.rest_api.calendar.resource.CalendarResource;
import church.lowlow.rest_api.common.aop.LogComponent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static church.lowlow.rest_api.common.util.WriterUtil.getWriter;
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

    @Autowired
    private LogComponent logComponent;

    /**
     * CREATE API
     */
    @PostMapping
    public ResponseEntity createCalendar(@RequestBody CalendarDto dto, Errors errors){

        // request param logging
        logComponent.calendarDtoLogging(dto);

        // check
        validation.validate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new CalendarErrorsResource(errors));

        // save
        Calendar calendar = modelMapper.map(dto, Calendar.class);
        calendar.setWriter(getWriter());
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
    public ResponseEntity getCalendarList(){
        List<Calendar> page = repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }


    @GetMapping("{id}")
    public ResponseEntity getCalendar(@PathVariable Integer id){

        Optional<Calendar> optional = repository.findById(id);
        Calendar notice = optional.orElseThrow(ArithmeticException::new);

        CalendarResource resource = new CalendarResource(notice);
        return ResponseEntity.ok(resource);
    }



    /**
     * UPDATE API
     */
    @PutMapping("/{id}")
    public ResponseEntity updateCalendar(@RequestBody @Valid CalendarDto dto, @PathVariable Integer id, Errors errors){

        // request param logging
        logComponent.calendarDtoLogging(dto);

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
    public ResponseEntity deleteCalendar(@PathVariable Integer id, Resource resource){

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
