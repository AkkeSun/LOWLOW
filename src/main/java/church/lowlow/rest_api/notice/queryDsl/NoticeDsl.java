package church.lowlow.rest_api.notice.queryDsl;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.notice.db.Notice;
import org.springframework.data.domain.Page;

public interface NoticeDsl {

    Page<Notice> getNoticePage(SearchDto searchDto, PagingDto pagingDto);
}
