package church.lowlow.security.domain.validation;

import church.lowlow.security.domain.dto.AccountDto;
import church.lowlow.security.domain.entity.Account;
import church.lowlow.security.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.List;


@Component
public class AccountValidation  {

    @Autowired
    AccountRepo accountRepo;

    public void createValidate(AccountDto dto, Errors errors) {

        boolean nullCheck = true;

        if(nullCheck){
            if(dto.getUsername() == null || dto.getUsername().equals(""))
                errors.rejectValue("username", "wrongName", "아이디를 입력하지 않았습니다");
            else if(dto.getPassword() == null || dto.getPassword().equals(""))
                errors.rejectValue("password", "wrongPassword", "비밀번호를 입력하지 않았습니다");
            else if(!dto.getPassword().equals(dto.getPasswordCheck()))
                errors.rejectValue("password", "wrongPassword", "비밀번호 확인이 올바르지 않습니다");
            else
                nullCheck = false;
        }
        if(nullCheck == false){
            Account account = accountRepo.findByUsername(dto.getUsername());
            if(account != null)
                errors.rejectValue("username", "wrongName", "이미 등록된 아이디입니다");
        }

    }


    public void updateValidate(AccountDto dto, Errors errors) {

        if(!(dto.getPassword() == null || dto.getPassword().equals("")) && !dto.getPassword().equals(dto.getPasswordCheck()))
            errors.rejectValue("password", "wrongPassword", "비밀번호 확인이 올바르지 않습니다");

    }
}


