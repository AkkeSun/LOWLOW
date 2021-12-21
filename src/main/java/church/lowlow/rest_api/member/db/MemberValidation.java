package church.lowlow.rest_api.member.db;

import church.lowlow.rest_api.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.regex.Pattern;
import static church.lowlow.rest_api.common.util.StringUtil.StringNullCheck;


/**
 * 성도 등록 유호성 검사
 */
@Component
public class MemberValidation {

    @Autowired
    MemberRepository memberRepository;

    public void createValidate(MemberDto dto, Errors errors) {

        basicValidate(dto, errors);

        Member checkMember = memberRepository.findByNameAndPhoneNumber(dto.getName(), dto.getPhoneNumber());
        if (checkMember != null)
            errors.rejectValue("name", "wrongName", "이미 등록된 교인입니다");

    }


    public void basicValidate(MemberDto dto, Errors errors){
        Pattern pattern = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");

        if(StringNullCheck(dto.getName()))
            errors.rejectValue("name", "wrongName", "이름은 비워둘 수 없습니다");
        else if(!pattern.matcher(dto.getPhoneNumber()).matches())
            errors.rejectValue("phoneNumber", "wrongPhoneNumber", "000-0000-0000 형식의 전화번호를 입력하세요");
        else if(dto.getGender() == null)
            errors.rejectValue("gender", "wrongGender", "성별은 비워둘 수 없습니다");
        else if(dto.getChurchOfficer() == null)
            errors.rejectValue("churchOfficer", "wrongChurchOfficer", "직분은 비워둘 수 없습니다");
        else if(dto.getRegiDate() == null)
            errors.rejectValue("regiDate", "wrongRegiDate", "등록일은 비워둘 수 없습니다");
    }
}
