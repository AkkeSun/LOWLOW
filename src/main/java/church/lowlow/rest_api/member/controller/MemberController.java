package church.lowlow.rest_api.member.controller;

import church.lowlow.rest_api.common.aop.LogComponent;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.db.MemberDto;
import church.lowlow.rest_api.member.repository.MemberRepository;
import church.lowlow.rest_api.member.resource.MemberErrorsResource;
import church.lowlow.rest_api.member.resource.MemberResource;
import church.lowlow.rest_api.member.service.MemberService;
import church.lowlow.rest_api.member.validation.MemberValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping(value = "/api/members", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class MemberController {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private LogComponent logComponent;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberValidation validation;

    /**************************
     *       CREATE API
     **************************/
    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberDto dto, Errors errors){

        // request param logging
        logComponent.memberDtoLogging(dto);

        // check
        validation.duplicate(dto, errors, "register");
        if(errors.hasErrors())
            return badRequest().body(new MemberErrorsResource(errors));

        Member newMember = memberService.createMember(dto, errors);
        URI createdUri = linkTo(MemberController.class).slash(newMember.getId()).toUri();

        // return
        MemberResource resource = new MemberResource(newMember);
        resource.add(linkTo(MemberController.class).slash(newMember.getId()).withRel("update-member"));
        resource.add(linkTo(MemberController.class).slash(newMember.getId()).withRel("delete-member"));

        return ResponseEntity.created(createdUri).body(resource);
    }



    /**************************
     *       READ API
     **************************/
    @GetMapping
    public ResponseEntity getMembers(PagedResourcesAssembler<Member> assembler,
                                     SearchDto searchDto, PagingDto pagingDto, @RequestParam(required = false) String is_MAttend) {

        // request param logging
        logComponent.searchDtoLogging(searchDto);
        logComponent.pagingDtoLogging(pagingDto);

        Page<Member> page = memberService.getMemberPage(searchDto, pagingDto, is_MAttend);

        var pagedResources = assembler.toResource(page, e -> new MemberResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity getMember(@PathVariable Integer id){

        // check
        Object serviceResponse = memberService.getMember(id);
        if(serviceResponse instanceof ResponseEntity)
            return (ResponseEntity) serviceResponse;

        Member member = (Member)serviceResponse;

        MemberResource resource = new MemberResource(member);
        resource.add(linkTo(MemberController.class).slash(member.getId()).withRel("update-event"));
        resource.add(linkTo(MemberController.class).slash(member.getId()).withRel("delete-event"));

        return ResponseEntity.ok(resource);
    }



    /**************************
     *       UPDATE API
     **************************/
    @PutMapping("/{id}")
    public ResponseEntity updateMembers(@RequestBody MemberDto dto, @PathVariable Integer id) {

        // request param logging
        logComponent.memberDtoLogging(dto);

        // check
        Object serviceResponse = memberService.updateMember(id, dto);
        if(serviceResponse instanceof ResponseEntity)
            return (ResponseEntity) serviceResponse;

        Member updateMember = (Member) serviceResponse;

        // return
        MemberResource resource = new MemberResource(updateMember);
        resource.add(linkTo(MemberController.class).slash(updateMember.getId()).withRel("delete-event"));

        return ResponseEntity.ok(resource);
    }



    /**************************
     *       DELETE API
     **************************/
    @DeleteMapping("/{id}")
    public ResponseEntity deleteMembers(@PathVariable Integer id) {

        // check
        Object serviceResponse = memberService.deleteMember(id);
        if(serviceResponse instanceof ResponseEntity)
            return (ResponseEntity) serviceResponse;

        Member deleteMember = (Member) serviceResponse;

        // return
        MemberResource resource = new MemberResource(deleteMember);
        return ResponseEntity.ok(resource);
    }
}
