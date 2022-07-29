package church.lowlow.jwt.validation;

import church.lowlow.jwt.entity.User;
import church.lowlow.jwt.entity.UserDto;
import church.lowlow.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Optional;


@Component
public class UserValidation {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void duplicated(UserDto dto, Errors errors, String type) {

        if("login".equals(type))
            loginValidation(dto, errors);
        else if ("register".equals(type))
            registerValidation(dto, errors);
        else if ("find".equals(type))
            findValidation(dto, errors);

    }

    private void loginValidation (UserDto dto, Errors errors) {
        Optional<User> checkRegister = repository.findByEmail(dto.getEmail());

        if (checkRegister.isEmpty())
            errors.rejectValue(null, "wrongEmail", "아이디 혹은 비밀번호가 올바르지 않습니다");
        else
        {
            User user = checkRegister.get();
            if ((!passwordEncoder.matches(dto.getPassword(), user.getPassword())))
                errors.rejectValue(null, "wrongPassword", "아이디 혹은 비밀번호가 올바르지 않습니다");
        }
    }

    private void registerValidation(UserDto dto, Errors errors) {
        if (repository.findByEmail(dto.getEmail()).isPresent())
            errors.rejectValue(null, "wrongEmail", "중복된 Email 입니다");

        if (!dto.getRole().equals("ROLE_DEV") && !dto.getRole().equals("ROLE_ADMIN") && !dto.getRole().equals("ROLE_UPLOADER"))
            errors.rejectValue(null, "wrongRole", "존재하지 않는 권한 입니다");
    }

    private void findValidation (UserDto dto, Errors errors){
        Optional<User> checkOptional = repository.findById(dto.getId());
        if(checkOptional.isEmpty())
            errors.rejectValue(null, "wrongId", "존재하는 아이디가 없습니다");
    }



}
