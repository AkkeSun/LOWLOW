package church.lowlow.rest_api.worshipVideo.queryDsl;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.worshipVideo.db.WorshipVideo;
import org.springframework.data.domain.Page;

public interface WorshipVideoDsl {
    Page<WorshipVideo> getWorshipVideList(SearchDto searchDto, PagingDto pagingDto);
}
