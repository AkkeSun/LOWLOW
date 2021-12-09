package church.lowlow.security.repository;

import church.lowlow.security.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepo extends JpaRepository<Role, Long> {


    @Query("select r from Role r where r.block=false")
    List<Role> getList();

    Role findByRoleName(String roleName);
}
