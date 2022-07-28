package church.lowlow.jwt.validation;

import church.lowlow.jwt.entity.UserDto;
import church.lowlow.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;


/**
 * 검색 시작일 종료일 확인
 */
@Component
public class UserValidation {

    @Autowired
    private UserRepository repository;

    public void duplicated(UserDto dto, Errors errors) {

        if (repository.findByEmail(dto.getEmail()).isPresent())
            errors.rejectValue(null, "wrongEmail", "중복된 Email 입니다");

        if (!dto.getRole().equals("ROLE_DEV") && !dto.getRole().equals("ROLE_ADMIN") && !dto.getRole().equals("ROLE_UPLOADER"))
            errors.rejectValue(null, "wrongRole", "존재하지 않는 권한 입니다");

    }
}
