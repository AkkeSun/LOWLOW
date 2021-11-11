package church.lowlow.rest_api.accounting.db;

import church.lowlow.rest_api.calendar.db.CalendarDto;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * 금액이 음수면 에러발생
 */
@Component
public class AccountingValidation implements Validator {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountingDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        AccountingDto dto = (AccountingDto) target;
        Member member = memberRepository.findByName(dto.getName());

        if(dto.getMoney() < 0)
            errors.rejectValue("money", "wrongMoney", "정확한 금액을 입력하세요");
        if(member == null)
            errors.rejectValue("name", "wrongName", "존재하지 않은 사람입니다");   // 생년월일 추가하기!!!!!!!!!!!!!!!!

    }
}
