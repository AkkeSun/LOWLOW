package church.lowlow.rest_api.notice.repository;

import church.lowlow.rest_api.notice.db.Notice;
import church.lowlow.rest_api.notice.queryDsl.NoticeDsl;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Integer>, NoticeDsl {
}
