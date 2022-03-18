package church.lowlow.rest_api.memberAttend.service;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.memberAttend.db.MemberAttend;
import church.lowlow.rest_api.memberAttend.db.MemberAttendDto;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface M_attendService {
    Map<String, Object> memberAttendListConvertor( Page<Tuple> memberAttendListData, PagingDto pagingDto);
    ResponseEntity createProcess(MemberAttend memberAttend);
    ResponseEntity updateProcess(MemberAttend memberAttend, Integer id);
}
