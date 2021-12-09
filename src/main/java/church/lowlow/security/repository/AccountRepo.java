package church.lowlow.security.repository;

import church.lowlow.security.domain.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    @Query("select a from Account a where a.block=false")
    List<Account> getList();
}
