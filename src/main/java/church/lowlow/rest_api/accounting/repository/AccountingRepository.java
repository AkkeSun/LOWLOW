package church.lowlow.rest_api.accounting.repository;

import church.lowlow.rest_api.accounting.db.Accounting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountingRepository extends JpaRepository<Accounting, Integer> {
}
