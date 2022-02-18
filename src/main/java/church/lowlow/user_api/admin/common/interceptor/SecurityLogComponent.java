package church.lowlow.user_api.admin.common.interceptor;

import church.lowlow.security.domain.dto.AccountDto;
import church.lowlow.security.domain.dto.ResourcesDto;
import church.lowlow.security.domain.dto.RoleDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * Request Parameter Logging Component
 */
@Component
@Log4j2
public class SecurityLogComponent {

    public void accountDtoLogging(AccountDto dto){
        log.info("[REQUEST] username : \"" + dto.getUsername() + "\"");
        log.info("[REQUEST] password : \"" + dto.getPassword() + "\"");
        log.info("[REQUEST] passwordCheck : \"" + dto.getPasswordCheck() + "\"");
        log.info("[REQUEST] belong : \"" + dto.getBelong() + "\"");
        log.info("[REQUEST] role : \"" + dto.getRole() + "\"");
    }

    public void roleDtoLogging(RoleDto dto){
        log.info("[REQUEST] roleName : \"" + dto.getRoleName() + "\"");
        log.info("[REQUEST] roleDesc : \"" + dto.getRoleDesc() + "\"");
        log.info("[REQUEST] roleNum : \"" + dto.getRoleNum() + "\"");
    }

    public void resourceDtoLogging(ResourcesDto dto){
        log.info("[REQUEST] resourceType : \"" + dto.getResourceType() + "\"");
        log.info("[REQUEST] resourceName : \"" + dto.getResourceName() + "\"");
        log.info("[REQUEST] orderNum : \"" + dto.getOrderNum() + "\"");
        log.info("[REQUEST] role : \"" + dto.getRole() + "\"");
    }

    public void idLogging(Long id){
        log.info("[REQUEST] id : \"" + id + "\"");
    }



}
