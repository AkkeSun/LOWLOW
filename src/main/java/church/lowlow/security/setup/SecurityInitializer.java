package church.lowlow.security.setup;

import church.lowlow.security.service.RoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;


@Component
@Order(0)
@Log4j2
public class SecurityInitializer implements ApplicationRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleHierarchyImpl roleHierarchy;

    @Override
    public void run(ApplicationArguments args)  {

        // Role roleHierarchy 정보 등록
        String allHierarchy = roleService.findAllHierarchy();
        log.info("[ROLE 계층적용] " + allHierarchy.replace("\n", " || "));
        roleHierarchy.setHierarchy(allHierarchy);
    }
}