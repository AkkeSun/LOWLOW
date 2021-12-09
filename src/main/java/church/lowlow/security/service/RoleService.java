package church.lowlow.security.service;

import church.lowlow.security.domain.dto.RoleDto;
import church.lowlow.security.domain.entity.Role;
import church.lowlow.security.repository.RoleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public Role getRole(long id) {
        Optional<Role> optional = roleRepo.findById(id);
        return optional.orElseThrow(NullPointerException::new);
    }

    @Transactional
    public List<Role> getRoles() {
        return roleRepo.getList();
    }

    @Transactional
    public void createRole(RoleDto dto) {
        Role role = modelMapper.map(dto, Role.class);
        roleRepo.save(role);
    }

    @Transactional
    public void updateRole(Long id, RoleDto roledto) {

        Optional<Role> optional = roleRepo.findById(id);
        optional.orElseThrow(NullPointerException::new);

        Role role = modelMapper.map(roledto, Role.class);
        role.setId(id);
        roleRepo.save(role);
    }


    /**
     * [Role 계층 권한 적용을 위한 format]
     * ROLE_ADMIN > ROLE_MANAGER
     * ROLE_MANAGER > ROLE_USER
     */
    @Transactional
    public String findAllHierarchy() {

        List<Role> roles = roleRepo.getList();
        StringBuffer concatedRoles = new StringBuffer();

        if (roles.size() <= 1)
            return "";

        for (int i = 0; i < roles.size(); i++) {
            if (i + 1 < roles.size()) {
                concatedRoles.append(roles.get(i).getRoleName());
                concatedRoles.append(" > ");
                concatedRoles.append(roles.get(i + 1).getRoleName());
                concatedRoles.append("\n");
            }
        }
        return concatedRoles.toString();
    }
}