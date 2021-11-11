package church.lowlow.rest_api.basicInfo.repository;

import church.lowlow.rest_api.basicInfo.db.BasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicInfoRepository extends JpaRepository<BasicInfo, Integer> {
}
