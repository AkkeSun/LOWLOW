package church.lowlow.rest_api.member.controller;

import church.lowlow.rest_api.common.aop.LogComponent;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.gallery.db.Gallery;
import church.lowlow.rest_api.member.db.MemberValidation;
import church.lowlow.rest_api.member.repository.MemberRepository;
import church.lowlow.rest_api.member.resource.MemberErrorsResource;
import church.lowlow.rest_api.member.resource.MemberResource;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.db.MemberDto;
import church.lowlow.user_api.common.fileProcess.service.basic.FileService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static church.lowlow.rest_api.common.util.WriterUtil.getWriter;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping(value = "/api/members", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class MemberController {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private FileService fileService;

    @Autowired
    private MemberValidation validation;

    @Autowired
    private  ModelMapper modelMapper;

    @Autowired
    private LogComponent logComponent;


    /**************************
     *       CREATE API
     **************************/
    @PostMapping
    @ApiOperation(value = "성도 등록", notes = "성도를 등록합니다", response = Member.class)
    public ResponseEntity createMember(@RequestBody MemberDto dto, Errors errors){

        // request param logging
        logComponent.memberDtoLogging(dto);

        // check
        validation.createValidate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new MemberErrorsResource(errors));

        // save
        Member member = modelMapper.map(dto, Member.class);
        member.setWriter( getWriter() );
        Member newMember = repository.save(member);
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
    @ApiOperation(value = "성도 리스트", notes = "성도 리스트를 출력합니다", response = Member.class)
    public ResponseEntity getMembers(PagedResourcesAssembler<Member> assembler,
                                     SearchDto searchDto, PagingDto pagingDto, @RequestParam(required = false) String is_MAttend) {

        // request param logging
        logComponent.searchDtoLogging(searchDto);
        logComponent.pagingDtoLogging(pagingDto);

        Page<Member> page = repository.getMemberPage(searchDto, pagingDto, is_MAttend);

        var pagedResources = assembler.toResource(page, e -> new MemberResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "성도", notes = "한 건의 성도 정보를 출력합니다", response = Member.class)
    public ResponseEntity getMember(@PathVariable Integer id){

        Optional<Member> optional = repository.findById(id);
        Member member = optional.orElseThrow(ArithmeticException::new);

        MemberResource resource = new MemberResource(member);
        return ResponseEntity.ok(resource);
    }



    /**************************
     *       UPDATE API
     **************************/
    @PutMapping("/{id}")
    @ApiOperation(value = "성도 수정", notes = "성도 정보를 수정합니다", response = Member.class)
    public ResponseEntity updateMembers(@RequestBody MemberDto dto, @PathVariable Integer id, Errors errors){

        // request param logging
        logComponent.memberDtoLogging(dto);

        // check
        validation.basicValidate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new MemberErrorsResource(errors));

        // update
        Member member = modelMapper.map(dto, Member.class);
        member.setWriter( getWriter() );
        member.setId(id);
        Member updateMember = repository.save(member);

        // return
        MemberResource resource = new MemberResource(updateMember);
        resource.add(linkTo(MemberController.class).slash(updateMember.getId()).withRel("delete-member"));

        return ResponseEntity.ok(resource);
    }



    /**************************
     *       DELETE API
     **************************/
    @DeleteMapping("/{id}")
    @ApiOperation(value = "성도 삭제", notes = "성도 정보를 삭제합니다", response = Member.class)
    public ResponseEntity deleteMembers(@PathVariable Integer id){

        // check
        Optional<Member> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

        // delete (block update)
        Member member = optional.get();
        member.setBlock(true);
        Member deleteMember = repository.save(member);
        fileService.deleteFile(member.getImage().getUploadName(), "member");

        // return
        MemberResource resource = new MemberResource(deleteMember);
        return ResponseEntity.ok(resource);
    }
}
