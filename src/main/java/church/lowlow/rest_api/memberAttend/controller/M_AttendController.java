package church.lowlow.rest_api.memberAttend.controller;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.memberAttend.db.MemberAttend;
import church.lowlow.rest_api.memberAttend.db.MemberAttendDto;
import church.lowlow.rest_api.memberAttend.db.MemberAttendListDto;
import church.lowlow.rest_api.memberAttend.repository.M_AttendRepository;
import church.lowlow.rest_api.common.aop.LogComponent;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.repository.MemberRepository;
import church.lowlow.rest_api.memberAttend.service.M_attendService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

import static church.lowlow.rest_api.common.util.WriterUtil.getWriter;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/memberAttend")
public class M_AttendController {

    @Autowired
    private M_AttendRepository m_attendRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LogComponent logComponent;

    @Autowired
    private M_attendService service;



    /**************************
     *   CREATE & UPDATE API
     **************************/
    @PostMapping
    @ApiOperation(value = "출석 내역 등록", notes = "출석 내역을 등록합니다", response = MemberAttend.class)
    public ResponseEntity createMemberAttend (@RequestBody MemberAttendDto dto){

        // request param logging
        logComponent.memberAttendDtoLogging(dto);

        // member Setting
        Optional<Member> optional = memberRepository.findById(dto.getMemberId());
        Member member = optional.orElseThrow(ArithmeticException::new);

        // data setting
        MemberAttend m_attend = modelMapper.map(dto, MemberAttend.class);
        m_attend.setMember(member);
        m_attend.setWriter( getWriter() );
        
        // update 유무 체크
        Optional<MemberAttend> updateCheck = m_attendRepository.findByMemberAndCheckDate(member, dto.getCheckDate());

        if(!updateCheck.isEmpty())
            return service.updateProcess(m_attend, updateCheck.get().getId());
        else
            return service.createProcess(m_attend);
    }




    /**************************
     *      SELECT API
     **************************/
    @GetMapping
    @ApiOperation(value = "출석 내역 리스트", notes = "출석 내역 리스트를 등록합니다", response = MemberAttend.class)
    public ResponseEntity getMemberAttendList(PagingDto pagingDto, SearchDto searchDto, String belong){

        // logging
        logComponent.M_AttendListLogging(pagingDto, searchDto, belong);

        // get DB Data
        Map<String, Object> dbMap = m_attendRepository.getMemberAttendList(searchDto, pagingDto, belong);

        // data convert
        Map<String, Object> returnMap = service.memberAttendListConvertor(dbMap, pagingDto);

        return ResponseEntity.ok().body(returnMap);
    }


    @GetMapping("/{checkDate}")
    @ApiOperation(value = "출석 내역", notes = "특정 날짜의 출석내용을 출력합니다", response = MemberAttend.class)
    public ResponseEntity getMemberAttendDetail(String belong, @PathVariable String checkDate){

        List<MemberAttend> list = m_attendRepository.getMemberAttendDetail(belong, checkDate);

        Map<String, Object> index = new HashMap<>();
        URI indexUri = linkTo(M_AttendController.class).slash(checkDate).toUri();
        index.put("rel", "index");
        index.put("href",indexUri);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("memberAttendList", list);
        resultMap.put("links", Arrays.asList(index));
        return ResponseEntity.ok().body(resultMap);
    }

}
