package church.lowlow.rest_api.memberAttend.queryDsl;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.memberAttend.db.MemberAttend;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;

import java.util.List;

public interface M_AttendDsl {

    Page<Tuple> getMemberAttendList(SearchDto searchDto, PagingDto pagingDto, String belong);
    List<MemberAttend> getMemberAttendDetail(String belong, String checkDate);
}
