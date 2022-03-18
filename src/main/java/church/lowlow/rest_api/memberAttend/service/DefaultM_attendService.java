package church.lowlow.rest_api.memberAttend.service;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.member.controller.MemberController;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.memberAttend.controller.M_AttendController;
import church.lowlow.rest_api.memberAttend.db.MemberAttend;
import church.lowlow.rest_api.memberAttend.db.MemberAttendDto;
import church.lowlow.rest_api.memberAttend.db.MemberAttendListDto;
import church.lowlow.rest_api.memberAttend.repository.M_AttendRepository;
import church.lowlow.rest_api.memberAttend.resource.M_AttendResource;
import com.querydsl.core.Tuple;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Service
public class DefaultM_attendService implements  M_attendService {

    @Autowired
    private M_AttendRepository m_attendRepository;


    // Page<Tuple> 데이터를 Map<String,Object> 타입으로 컨버팅
    @Transactional
    public Map<String, Object> memberAttendListConvertor( Page<Tuple> memberAttendListData, PagingDto pagingDto ) {

        List<Tuple> content = memberAttendListData.getContent();
        Pageable pageable = memberAttendListData.getPageable();

        List<MemberAttendListDto> maList = getMemberAttendList(content);
        PagingDto pageData = getPageData(pageable, pagingDto, maList);

        return getResultMap(maList, pageData);
    }

    @Transactional
    public ResponseEntity createProcess(MemberAttend memberAttend) {

        MemberAttend newMemberAttend = m_attendRepository.save(memberAttend);
        URI createdUri = linkTo(M_AttendController.class).slash(newMemberAttend.getId()).toUri();

        M_AttendResource resource = new M_AttendResource(newMemberAttend);
        Map<String, Object> returnMap = new HashMap<>();

        returnMap.put("resource", resource);
        returnMap.put("createdUri", createdUri);

        return ResponseEntity.created(createdUri).body(resource);
    }


    @Transactional
    public ResponseEntity updateProcess(MemberAttend memberAttend, Integer id) {

        memberAttend.setId(id);
        MemberAttend updateData = m_attendRepository.save(memberAttend);
        M_AttendResource resource = new M_AttendResource(updateData);

        return  ResponseEntity.ok(resource);
    }






    private List<MemberAttendListDto> getMemberAttendList (List<Tuple> content) {

        List<MemberAttendListDto> maList = new ArrayList<>();
        for(Tuple tuple : content) {
            Object [] obj = tuple.toArray();
            MemberAttendListDto listDto = MemberAttendListDto.builder()
                    .total((Long)obj[0])
                    .checkDate((LocalDate)obj[1])
                    .isAttendTrue((Long)obj[2])
                    .isAttendFalse((Long) obj[3])
                    .build();
            maList.add(listDto);
        }
        return maList;
    }



    private PagingDto getPageData(Pageable pageable, PagingDto pagingDto, List<MemberAttendListDto> content) {

        int totalPages = (content.size()-1) / ( (int)pageable.getPageSize() ) + 1;
        return PagingDto.builder().nowPage(pagingDto.getNowPage()).totalPages(totalPages).build();

    }

    private Map<String, Object> getResultMap(List<MemberAttendListDto> maList, PagingDto page) {

        Map<String, Object> index = new HashMap<>();
        URI indexUri = linkTo(M_AttendController.class).toUri();
        index.put("rel", "index");
        index.put("href",indexUri);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("page", page);
        resultMap.put("memberAttendList", maList);
        resultMap.put("links", Arrays.asList(index));

        return resultMap;
    }

}
