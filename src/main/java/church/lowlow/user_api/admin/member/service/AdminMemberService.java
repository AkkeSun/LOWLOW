package church.lowlow.user_api.admin.member.service;

import church.lowlow.rest_api.member.db.Member;

public interface AdminMemberService {

    Member getMember(Long id);

}
