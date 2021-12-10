package church.lowlow.security.domain.validation;

import church.lowlow.security.domain.dto.RoleDto;
import church.lowlow.security.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;


@Component
public class RoleValidation {

    @Autowired
    RoleRepo roleRepo;

    public void createValidate(RoleDto dto, Errors errors) {

        boolean nullCheck = true;

        if(nullCheck){
            if(dto.getRoleName() == null || dto.getRoleName().equals(""))
                errors.rejectValue("roleName", "wrongRoleName", "권한이름을 입력하지 않았습니다");
            if(!dto.getRoleName().contains("ROLE_"))
                errors.rejectValue("roleName", "wrongRoleName", "권한이름은 'ROLE_' 로 시작해야합니다");
            else
                nullCheck = false;
        }
        if(nullCheck == false){
            if(roleRepo.findByRoleName(dto.getRoleName()) != null )
                errors.rejectValue("roleName", "wrongRoleName", "이미 등록된 권한이름 입니다");
            else if(roleRepo.findByRoleNum(dto.getRoleNum()) != null)
                errors.rejectValue("roleNum", "wrongRoleNum", "동일한 권한순서가 존재합니다");
        }

    }

    public void updateValidate(RoleDto dto, Errors errors) {

        if(roleRepo.findByRoleNum(dto.getRoleNum()) != null)
            errors.rejectValue("roleNum", "wrongRoleNum", "동일한 권한 순서가 존재합니다");
    }
}


