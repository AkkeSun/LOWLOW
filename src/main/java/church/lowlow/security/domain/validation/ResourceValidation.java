package church.lowlow.security.domain.validation;

import church.lowlow.security.domain.dto.ResourcesDto;
import church.lowlow.security.domain.dto.RoleDto;
import church.lowlow.security.domain.entity.Resources;
import church.lowlow.security.domain.entity.Role;
import church.lowlow.security.repository.ResourcesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static church.lowlow.rest_api.common.util.StringUtil.StringNullCheck;


@Component
public class ResourceValidation {

    @Autowired
    ResourcesRepo resourcesRepo;

    public void createValidate(ResourcesDto dto, Errors errors) {

        boolean nullCheck = true;

        if(nullCheck){
            if(StringNullCheck(dto.getResourceType()))
                errors.rejectValue("resourceType", "wrongResourceType", "리소스타입을 입력하지 않았습니다");
            else if(StringNullCheck(dto.getResourceName()))
                errors.rejectValue("resourceName", "wrongResourceName", "리소스이름을 입력하지 않았습니다");
            else if(StringNullCheck(dto.getRoleName()))
                errors.rejectValue("roleName", "wrongRoleName", "권한이름을 입력하지 않았습니다");
            else
                nullCheck = false;
        }
        if(nullCheck == false){
            if(resourcesRepo.findByResourceName(dto.getResourceName()) != null )
                errors.rejectValue("resourceName", "wrongResourceName", "이미 등록된 리소스 이름 입니다");
            else if(resourcesRepo.findByOrderNum(dto.getOrderNum()) != null)
                errors.rejectValue("orderNum", "wrongOrderNum", "동일한 순서가 존재합니다");
        }

    }

    public void updateValidate(ResourcesDto dto, Long id, Errors errors) {

        Resources byResourceName = resourcesRepo.findByResourceName(dto.getResourceName());
        Resources byOrderNum = resourcesRepo.findByOrderNum(dto.getOrderNum());

        if(byResourceName != null){
            if(byResourceName.getId() != id)
                errors.rejectValue("resourceName", "wrongResourceName", "이미 등록된 리소스 이름 입니다");
        }
        if(byOrderNum != null){
            if(byOrderNum.getId() != id)
                errors.rejectValue("orderNum", "wrongOrderNum", "동일한 순서가 존재합니다");
        }

    }
}


