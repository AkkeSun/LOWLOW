package church.lowlow.security.setup;


import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.repository.MemberRepository;
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

    @Autowired
    private ResourcesRepo resourcesRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    //=================================================


    @Override
    @Transactional
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        // SECURITY ROLE
        createRoleIfNotFound("ROLE_DEV", "개발자", 0);
        createRoleIfNotFound("ROLE_MST", "마스터", 1);
        createRoleIfNotFound("ROLE_GAF", "총무", 2);
        createRoleIfNotFound("ROLE_ULD", "업로더", 3);
        createRoleIfNotFound("ROLE_LDR", "리더", 4);

        // SECURITY USER
        createUserIfNotFound("lowlow", "1111", "ROLE_DEV");

        // SECURITY RESOURCE
        createResourceIfNotFound("/admin/security/**", "url", "ROLE_DEV", 0);
        createResourceIfNotFound("/admin/basicInfo/**", "url", "ROLE_DEV", 1);
        createResourceIfNotFound("/admin/members/**", "url", "ROLE_LDR", 2);
        createResourceIfNotFound("/admin/accounting/**", "url", "ROLE_GAF", 3);
        createResourceIfNotFound("/admin/gallery/**", "url", "ROLE_ULD", 4);
        createResourceIfNotFound("/admin/calendar/**", "url", "ROLE_ULD", 5);
        createResourceIfNotFound("/admin/notice/**", "url", "ROLE_ULD", 6);
        createResourceIfNotFound("/admin/weekly/**", "url", "ROLE_ULD", 7);
        createResourceIfNotFound("/admin/worshipVideo/**", "url", "ROLE_ULD", 8);
        createResourceIfNotFound("/admin/**", "url", "ROLE_LDR", 9);

        // MEMBER
        createMemberIfNotFound("익명");

    }

    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc, int ruleNum) {
        Role role = roleRepo.findByRoleName(roleName);

        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .roleNum(ruleNum)
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
    public Resources createResourceIfNotFound(String resourceName, String resourceType, String roleName, int orderNum) {
        Resources resources = resourcesRepo.findByResourceName(resourceName);

        if (resources == null) {
            Role role = roleRepo.findByRoleName(roleName);

            resources = Resources.builder()
                    .resourceName(resourceName)
                    .resourceRole(role)
                    .resourceType(resourceType)
                    .orderNum(orderNum)
                    .build();
        }
        return resourcesRepo.save(resources);
    }

    @Transactional
    public Member createMemberIfNotFound(String name) {
        Member member = memberRepository.findByName(name);

        if (member == null)
            member = Member.builder().name(name).build();

        return memberRepository.save(member);
    }

}