package church.lowlow.rest_api.member.queryDsl;

import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MemberDsl {
    Page<Member> getMemberPage(SearchDto searchDto, Pageable pageable);
}
