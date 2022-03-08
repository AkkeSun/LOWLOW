package church.lowlow.rest_api.MemberAttend.controller;

import church.lowlow.rest_api.MemberAttend.db.MemberAttend;
import church.lowlow.rest_api.MemberAttend.db.MemberAttendDto;
import church.lowlow.rest_api.MemberAttend.repository.M_AttendRepository;
import church.lowlow.rest_api.MemberAttend.resource.M_AttendResource;
import church.lowlow.rest_api.common.aop.LogComponent;
import church.lowlow.rest_api.member.controller.MemberController;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.db.MemberDto;
import church.lowlow.rest_api.member.resource.MemberErrorsResource;
import church.lowlow.rest_api.member.resource.MemberResource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static church.lowlow.rest_api.common.util.WriterUtil.getWriter;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class M_AttendController {

    @Autowired
    private M_AttendRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LogComponent logComponent;


    /**************************
     *       CREATE API
     **************************/
    @PostMapping
    public ResponseEntity createMemberAttend (@RequestBody MemberAttendDto dto){

        // request param logging
        logComponent.memberAttendDtoLogging(dto);

        // save
        MemberAttend m_attend = modelMapper.map(dto, MemberAttend.class);
        m_attend.setWriter( getWriter() );
        MemberAttend newMemberAttend = repository.save(m_attend);
        URI createdUri = linkTo(M_AttendController.class).slash(newMemberAttend.getId()).toUri();

        // return
        M_AttendResource resource = new M_AttendResource(newMemberAttend);
        resource.add(linkTo(MemberController.class).slash(newMemberAttend.getId()).withRel("update-member"));
        resource.add(linkTo(MemberController.class).slash(newMemberAttend.getId()).withRel("delete-member"));

        return ResponseEntity.created(createdUri).body(resource);
    }



}
