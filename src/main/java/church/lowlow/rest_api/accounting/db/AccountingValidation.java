package church.lowlow.rest_api.accounting.db;

import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * 금액이 음수인지, 헌금한 사람이 유효한지 확인
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

        if(dto.getMoney() < 0)
            errors.rejectValue("money", "wrongMoney", "정확한 금액을 입력하세요");

        if(!dto.getName().equals("익명"))
            notUnKnowUserValidation(dto, errors);
    }


    public void notUnKnowUserValidation(AccountingDto dto, Errors errors){
        Member member = memberRepository.findByName(dto.getName());

        if(dto.getBirthYear()==0)
            errors.rejectValue("birthYear", "wrongBirthYear", "또래(생년)를 입력하세요");
        else if (member == null)
            errors.rejectValue("name", "wrongName", "존재하지 않은 사람입니다");
        else if(dto.getBirthYear() != member.getBirthYear())
            errors.rejectValue("birthDay", "wrongBirthDay", "존재하지 않은 사람입니다");
    }
}
