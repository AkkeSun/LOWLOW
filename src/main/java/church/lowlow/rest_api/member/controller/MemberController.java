package church.lowlow.rest_api.member.controller;

import church.lowlow.rest_api.member.db.MemberValidation;
import church.lowlow.rest_api.member.repository.MemberRepository;
import church.lowlow.rest_api.member.resource.MemberErrorsResource;
import church.lowlow.rest_api.member.resource.MemberResource;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.db.MemberDto;
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
@RequestMapping(value = "/api/members", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class MemberController {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private MemberValidation validation;

    @Autowired
    private  ModelMapper modelMapper;

    /**
     * CREATE API
     */
    @PostMapping
    public ResponseEntity createMember(@RequestBody @Valid MemberDto dto,
                                       Errors errors){
        // check
        if(errors.hasErrors())
            return badRequest().body(new MemberErrorsResource(errors));
        validation.validate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new MemberErrorsResource(errors));

        // save
        Member member = modelMapper.map(dto, Member.class);
        Member newMember = repository.save(member);
        URI createdUri = linkTo(MemberController.class).slash(newMember.getId()).toUri();

        // return
        MemberResource resource = new MemberResource(newMember);
        resource.add(linkTo(MemberController.class).slash(newMember.getId()).withRel("update-member"));
        resource.add(linkTo(MemberController.class).slash(newMember.getId()).withRel("delete-member"));

        return ResponseEntity.created(createdUri).body(resource);
    }



    /**
     * READ API
     */
    @GetMapping
    public ResponseEntity getMembers(Pageable pageable,
                                     PagedResourcesAssembler<Member> assembler){
        Page<Member> page = repository.findAll(pageable);
        var pagedResources = assembler.toResource(page, e -> new MemberResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity getMember(@PathVariable Integer id){
        Optional<Member> optional = repository.findById(id);
        Member member = optional.orElseThrow(ArithmeticException::new);

        // 로그인 유무 체크 후 로그인 했으면 update, delete url 넣어주기
        MemberResource resource = new MemberResource(member);
        return ResponseEntity.ok(resource);
    }


    
    /**
     * UPDATE API
     */
    @PutMapping("/{id}")
    public ResponseEntity updateMembers(@RequestBody @Valid MemberDto dto,
                                        @PathVariable Integer id,
                                        Errors errors){

        // check
        Optional<Member> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();
        if(errors.hasErrors())
            return badRequest().body(new MemberErrorsResource(errors));

        // save
        Member member = modelMapper.map(dto, Member.class);
        member.setId(id);
        Member updateMember = repository.save(member);

        // return
        MemberResource resource = new MemberResource(updateMember);
        resource.add(linkTo(MemberController.class).slash(updateMember.getId()).withRel("delete-member"));

        return ResponseEntity.ok(resource);
    }

    
    
    /**
     * DELETE API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteMembers(@PathVariable Integer id, Resource resource){

        // check
        Optional<Member> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

        // delete
        repository.deleteById(id);

        // return
        resource.add(linkTo(MemberController.class).withRel("index"));
        return ResponseEntity.ok(resource);
    }
}