package church.lowlow.rest_api.weekly.repository;

import church.lowlow.rest_api.weekly.db.Weekly;
import church.lowlow.rest_api.weekly.queryDsl.WeeklyDsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyRepository extends JpaRepository<Weekly, Integer>, WeeklyDsl {
}
