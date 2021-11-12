package church.lowlow.rest_api.member.db;

import church.lowlow.rest_api.accounting.db.AccountingDto;
import church.lowlow.rest_api.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * 이미 등록된 교인인지 확인
 */
@Component
public class MemberValidation implements Validator {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountingDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        MemberDto dto = (MemberDto) target;
        Member checkMember = memberRepository.findByNameAndBirthYear(dto.getName(), dto.getBirthYear());
        if (checkMember != null)
            errors.rejectValue("name", "wrongName", "이미 등록된 교인입니다");

    }
}
