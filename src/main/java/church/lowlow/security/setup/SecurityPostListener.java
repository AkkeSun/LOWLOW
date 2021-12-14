package church.lowlow.security.setup;


import church.lowlow.security.domain.entity.Account;
import church.lowlow.security.domain.entity.Resources;
import church.lowlow.security.domain.entity.Role;
import church.lowlow.security.repository.AccountRepo;
import church.lowlow.security.repository.ResourcesRepo;
import church.lowlow.security.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;


/**
 * Runner 실행 전 기본 데이터 입력 이벤트
 */
@Component
public class SecurityPostListener implements ApplicationListener<ApplicationStartedEvent> {

    //================= 데이터 입력 =================

    // ROLE
    private final String ROLE_NAME = "ROLE_DEV";
    private final String ROLE_DESC = "개발자";

    // USER
    private final String USER_NAME = "lowlow";
    private final String PASSWORD = "1111";

    // RESOURCE
    private final String RESOURCE_NAME = "/admin/**";
    private final String RESOURCE_TYPE = "url";


    //=================================================


    @Autowired
    private ResourcesRepo resourcesRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //=================================================


    @Override
    @Transactional
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        createRoleIfNotFound(ROLE_NAME, ROLE_DESC);
        createUserIfNotFound(USER_NAME, PASSWORD, ROLE_NAME);
        createResourceIfNotFound(RESOURCE_NAME, RESOURCE_TYPE, ROLE_NAME);

    }

    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc) {
        Role role = roleRepo.findByRoleName(roleName);

        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .roleNum(0)
                    .build();
        }
        return roleRepo.save(role);
    }

    @Transactional
    public Account createUserIfNotFound(String userName, String password, String roleName) {

        Account account = accountRepo.findByUsername(userName);

        if (account == null) {
            Role role = roleRepo.findByRoleName(roleName);

            account = Account.builder()
                    .username(userName)
                    .password(passwordEncoder.encode(password))
                    .userRole(role)
                    .build();
        }
        return accountRepo.save(account);
    }

    @Transactional
    public Resources createResourceIfNotFound(String resourceName, String resourceType, String roleName) {
        Resources resources = resourcesRepo.findByResourceName(resourceName);

        if (resources == null) {
            Role role = roleRepo.findByRoleName(roleName);

            resources = Resources.builder()
                    .resourceName(resourceName)
                    .resourceRole(role)
                    .resourceType(resourceType)
                    .orderNum(0)
                    .build();
        }
        return resourcesRepo.save(resources);
    }

}