package church.lowlow.rest_api.accounting.controller;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.accounting.db.AccountingDto;
import church.lowlow.rest_api.accounting.repository.AccountingRepository;
import church.lowlow.rest_api.accounting.resource.AccountingErrorsResource;
import church.lowlow.rest_api.accounting.resource.AccountingResource;
import church.lowlow.rest_api.member.repository.MemberRepository;
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
public class AccountingController {

    @Autowired
    private AccountingRepository accountingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;


    /**
     * CREATE API
     */
    @PostMapping
    public ResponseEntity createWorshipVideo(@RequestBody @Valid AccountingDto dto,
                                              Errors errors){
        // check
        if(errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));
        if(errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));

        // save

        Accounting calendar = modelMapper.map(dto, Accounting.class);
        Accounting newCalendar = accountingRepository.save(calendar);
        URI createdUri = linkTo(AccountingController.class).slash(newCalendar.getId()).toUri();

        // return
        AccountingResource resource = new AccountingResource(newCalendar);
        resource.add(linkTo(AccountingController.class).slash(newCalendar.getId()).withRel("update-calendar"));
        resource.add(linkTo(AccountingController.class).slash(newCalendar.getId()).withRel("delete-calendar"));

        return ResponseEntity.created(createdUri).body(resource);
    }



    /**
     * READ API
     */
    @GetMapping
    public ResponseEntity getWorshipVideo(Pageable pageable,
                                          PagedResourcesAssembler<Accounting> assembler){
        Page<Accounting> page = accountingRepository.findAll(pageable);
        var pagedResources = assembler.toResource(page, e -> new AccountingResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity geWorshipVideo(@PathVariable Integer id){
        Optional<Accounting> optional = accountingRepository.findById(id);
        Accounting notice = optional.orElseThrow(ArithmeticException::new);

        // 로그인 유무 체크 후 로그인 했으면 update, delete url 넣어주기
        AccountingResource resource = new AccountingResource(notice);
        return ResponseEntity.ok(resource);
    }



    /**
     * UPDATE API
     */
    @PutMapping("/{id}")
    public ResponseEntity updateWorshipVideo(@RequestBody @Valid AccountingDto dto,
                                             @PathVariable Integer id,
                                             Errors errors){

        // check
        Optional<Accounting> optional = accountingRepository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();
        if(errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));

        // save
        Accounting notice = modelMapper.map(dto, Accounting.class);
        notice.setId(id);
        Accounting updateWorshipVideo = accountingRepository.save(notice);

        // return
        AccountingResource resource = new AccountingResource(updateWorshipVideo);
        resource.add(linkTo(AccountingController.class).slash(updateWorshipVideo.getId()).withRel("delete-worshipVideo"));

        return ResponseEntity.ok(resource);
    }



    /**
     * DELETE API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteWorshipVideo(@PathVariable Integer id, Resource resource){

        // check
        Optional<Accounting> optional = accountingRepository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

        // delete
        accountingRepository.deleteById(id);

        // return
        resource.add(linkTo(AccountingController.class).withRel("index"));
        return ResponseEntity.ok(resource);
    }
}
