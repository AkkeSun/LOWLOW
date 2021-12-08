package church.lowlow.security.domain.validation;

import church.lowlow.security.domain.dto.AccountDto;
import church.lowlow.security.domain.entity.Account;
import church.lowlow.security.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AccountValidation implements Validator {

    @Autowired
    AccountRepo accountRepo;

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountDto dto = (AccountDto) target;
        Account check = accountRepo.findByUsername(dto.getUsername());
        if (check != null)
            errors.rejectValue("username", "wrongName", "이미 등록된 아이디입니다");
    }
}
