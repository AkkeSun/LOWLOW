package church.lowlow.rest_api.weekly.queryDsl;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.weekly.db.Weekly;
import org.springframework.data.domain.Page;

public interface WeeklyDsl {
    Page<Weekly> getWeeklyList(SearchDto searchDto, PagingDto pagingDto);
}
