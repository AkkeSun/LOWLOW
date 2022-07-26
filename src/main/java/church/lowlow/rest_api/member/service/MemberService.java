package church.lowlow.rest_api.member.service;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.db.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.validation.Errors;

public interface MemberService {

    Object createMember(MemberDto dto, Errors errors);
    Page<Member> getMemberPage(SearchDto searchDto, PagingDto pagingDto, String is_MAttend);
    Member getMember(Integer id);
    Object updateMember (Integer id, MemberDto dto, Errors errors);
    Object deleteMember (Integer id);

}
