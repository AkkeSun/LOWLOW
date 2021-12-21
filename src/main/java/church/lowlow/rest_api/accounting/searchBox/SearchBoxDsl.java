package church.lowlow.rest_api.accounting.searchBox;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.common.entity.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;

public interface SearchBoxDsl {

    // 검색 유틸 함수
    Page<Accounting> searchBox(SearchDto searchDto, int nowPage) throws ParseException;
}
