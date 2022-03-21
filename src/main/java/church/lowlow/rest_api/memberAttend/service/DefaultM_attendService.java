package church.lowlow.rest_api.memberAttend.service;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.memberAttend.controller.M_AttendController;
import church.lowlow.rest_api.memberAttend.db.MemberAttend;
import church.lowlow.rest_api.memberAttend.db.MemberAttendListDto;
import church.lowlow.rest_api.memberAttend.repository.M_AttendRepository;
import church.lowlow.rest_api.memberAttend.resource.M_AttendResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Service
public class DefaultM_attendService implements  M_attendService {

    @Autowired
    private M_AttendRepository m_attendRepository;


    @Transactional
    public Map<String, Object> memberAttendListConvertor(Map<String, Object> map, PagingDto pagingDto) {

        ArrayList<MemberAttendListDto> content = (ArrayList) map.get("memberAttendList");
        int totalContentCnt = (int)map.get("totalContentCnt");
        PagingDto pageData = getPageData(content, totalContentCnt, pagingDto);

        return getResultMap(content, pageData);
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



    private PagingDto getPageData(List<MemberAttendListDto> content, int totalContentCnt, PagingDto pagingDto ) {

        int totalPages = (totalContentCnt-1) / 10 + 1;
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
