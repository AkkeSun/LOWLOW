package church.lowlow.rest_api.member.queryDsl;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.Member;
import org.springframework.data.domain.Page;

public interface MemberDsl {
    Page<Member> getMemberPage(SearchDto searchDto, PagingDto pagingDto);
}
