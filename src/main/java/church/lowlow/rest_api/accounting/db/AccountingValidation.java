package church.lowlow.rest_api.accounting.db;

import church.lowlow.rest_api.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;


@Component
public class AccountingValidation  {

    @Autowired
    MemberRepository memberRepository;

    public void validate(Object target, Errors errors) {

        AccountingDto dto = (AccountingDto) target;

        if(dto.getMoney() < 0)
            errors.rejectValue("money", "wrongMoney", "정확한 금액을 입력하세요");
        if(dto.getMemberId() == null)
            errors.rejectValue("memberId", "wrongMemberId", "이름을 입력 후 확인하세요");
        if(dto.getOfferingDate() == null)
            errors.rejectValue("offeringDate", "wrongOfferingDate", "헌금일을 입력하세요");
    }
}
