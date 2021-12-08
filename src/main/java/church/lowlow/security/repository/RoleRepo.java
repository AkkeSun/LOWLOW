package church.lowlow.security.repository;

import church.lowlow.security.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}
