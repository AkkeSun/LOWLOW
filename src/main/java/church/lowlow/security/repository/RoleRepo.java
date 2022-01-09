package church.lowlow.security.repository;

import church.lowlow.security.domain.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepo extends JpaRepository<Role, Long> {


    @Query("select r from Role r where r.block=false order by r.roleNum")
    List<Role> getList();

    @Query("select r from Role r where r.block=false order by r.roleNum")
    Page<Role> getListForPage(Pageable pageable);

    @Query("select r from Role r where r.roleNum=:roleNum and r.block=false")
    Role findByRoleNum(int roleNum);

    Role findByRoleName(String roleName);
}
