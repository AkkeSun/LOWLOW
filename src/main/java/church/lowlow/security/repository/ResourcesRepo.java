package church.lowlow.security.repository;

import church.lowlow.security.domain.entity.Resources;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ResourcesRepo extends JpaRepository<Resources, Long> {

    @EntityGraph(value = "getRole") // 자식 객채를 얻기 위한 설정
    @Query("select r from Resources r where r.block = false order by r.orderNum")
    Page<Resources> getListForPage(Pageable pageable);

    @EntityGraph(value = "getRole")
    @Query("select r from Resources r where r.resourceType = 'url' and r.block = false order by r.orderNum")
    List<Resources> findAllUrlResources();

    @Query("select r from Resources r where r.resourceName = :resourcesName and r.block = false")
    Resources findByResourceName(String resourcesName);

    @Query("select r from Resources r where r.orderNum = :num and r.block = false")
    Resources findByOrderNum(int num);
}
