package church.lowlow.rest_api.member.controller;

import church.lowlow.rest_api.common.entity.Files;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.common.entity.Writer;
import church.lowlow.rest_api.member.db.ChurchOfficer;
import church.lowlow.rest_api.member.db.MemberValidation;
import church.lowlow.rest_api.member.repository.MemberRepository;
import church.lowlow.rest_api.member.resource.MemberErrorsResource;
import church.lowlow.rest_api.member.resource.MemberResource;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.db.MemberDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.text.ParseException;
import java.util.Optional;

import static church.lowlow.rest_api.common.util.StringUtil.StringNullCheck;
import static church.lowlow.rest_api.common.util.WriterUtil.getWriter;
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
    public ResponseEntity createMember(@RequestBody MemberDto dto,
                                       Errors errors){
        // check
        validation.createValidate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new MemberErrorsResource(errors));

        // save
        Member member = modelMapper.map(dto, Member.class);
        member.setWriter( getWriter() );
        member.setImage(Files.builder().originalName(dto.getOriginalName()).uploadName(dto.getUploadName()).build());
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
    public ResponseEntity getMembers(PagedResourcesAssembler<Member> assembler,
                                     SearchDto searchDto, int nowPage) {

        Page<Member> page = searchData(searchDto, nowPage);

        var pagedResources = assembler.toResource(page, e -> new MemberResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity getMember(@PathVariable Integer id){
        Optional<Member> optional = repository.findById(id);
        Member member = optional.orElseThrow(ArithmeticException::new);

        MemberResource resource = new MemberResource(member);
        return ResponseEntity.ok(resource);
    }


    
    /**
     * UPDATE API
     */
    @PutMapping("/{id}")
    public ResponseEntity updateMembers(@RequestBody MemberDto dto,
                                        @PathVariable Integer id,
                                        Errors errors){

        // check
        validation.basicValidate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new MemberErrorsResource(errors));

        // update
        Member member = modelMapper.map(dto, Member.class);
        member.setWriter( getWriter() );
        member.setImage(Files.builder().originalName(dto.getOriginalName()).uploadName(dto.getUploadName()).build());
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




    /*****************************************
     *              Util Method
     ******************************************/
    // ============== 페이지 검색 함수 ==============
    public Page<Member> searchData(SearchDto searchDto, int nowPage){

        Pageable pageable = PageRequest.of(nowPage, 10);
        String searchId = searchDto.getSearchId();
        String searchData = searchDto.getSearchData();
        if(!StringNullCheck(searchData)){
            switch(searchId){
                case "belong"        : return repository.findAllByBelong(searchData, pageable);
                case "churchOfficer" : return repository.findAllByChurchOfficer(churchOfficerChangeToEng(searchData), pageable);
                case "name"          : return repository.findAllByName(searchData, pageable);
            }
        }
        else
            return repository.findAll(pageable);
        return null;
    }

    // ================= 직분 영어 변경 함수  ====================
    public ChurchOfficer churchOfficerChangeToEng(String churchOfficer){

        switch( churchOfficer ){
            case "평신도"  : return ChurchOfficer.valueOf("LAYMAN");
            case "집사"    : return ChurchOfficer.valueOf("DEACON");
            case "안수집사" : return ChurchOfficer.valueOf("ORDAINED_DEACON");
            case "권사"    : return ChurchOfficer.valueOf("SENIOR_DEACONESS");
            case "장로"    : return ChurchOfficer.valueOf("ELDER");
            case "전도사"   : return ChurchOfficer.valueOf("JUNIOR_PASTOR");
            case "부목사"   : return ChurchOfficer.valueOf("ASSISTANT_PASTOR");
            case "담임목사"  : return ChurchOfficer.valueOf("SENIOR_PASTOR");
            case "사모"     : return ChurchOfficer.valueOf("WIFE");
        };
        return ChurchOfficer.valueOf("NULL");
    }


}