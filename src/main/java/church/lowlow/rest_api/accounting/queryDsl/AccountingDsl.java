package church.lowlow.rest_api.accounting.queryDsl;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.Member;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountingDsl {

    Page<Accounting> getAccountingPage(SearchDto searchDto, PagingDto pagingDto);
    List<Tuple> getAccountingStatistics (SearchDto searchDto, String countKind);
}
