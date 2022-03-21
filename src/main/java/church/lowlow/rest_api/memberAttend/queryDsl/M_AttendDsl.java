package church.lowlow.rest_api.memberAttend.queryDsl;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.memberAttend.db.MemberAttend;
import church.lowlow.rest_api.memberAttend.db.MemberAttendListDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface M_AttendDsl {

    Map<String, Object> getMemberAttendList(SearchDto searchDto, PagingDto pagingDto, String belong);
    List<MemberAttend> getMemberAttendDetail (String belong, String checkDate);
}
