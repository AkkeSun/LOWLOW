package church.lowlow.security.repository;

import church.lowlow.security.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
}
