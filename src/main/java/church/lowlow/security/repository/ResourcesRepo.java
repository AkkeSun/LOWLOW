package church.lowlow.security.repository;

import church.lowlow.security.domain.entity.Account;
import church.lowlow.security.domain.entity.Resources;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ResourcesRepo extends JpaRepository<Resources, Long> {

    @EntityGraph(value = "getRole")
    @Query("select r from Resources r order by r.orderNum")
    List<Resources> findAllResources();

    @EntityGraph(value = "getRole")
    @Query("select r from Resources r order by r.orderNum")
    Page<Resources> getListForPage(Pageable pageable);

    @EntityGraph(value = "getRole")
    @Query("select r from Resources r where r.resourceType = 'url' and r.block = false order by r.orderNum desc")
    List<Resources> findAllUrlResources();

    @EntityGraph(value = "getRole")
    @Query("select r from Resources r where r.resourceType = 'method' and r.block = false order by r.orderNum desc")
    List<Resources> findAllMethodResources();

    Resources findByResourceName(String resourcesName);
}
