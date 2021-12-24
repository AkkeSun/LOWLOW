package church.lowlow.rest_api.accounting.searchDsl;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface AccountingSearchDsl {

    Page<Accounting> getAccountingPage(SearchDto searchDto, PagingDto pagingDto);
    List<Tuple> getOfferingMoneyCount(SearchDto searchDto);
}
