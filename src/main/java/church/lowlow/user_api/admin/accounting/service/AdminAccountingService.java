package church.lowlow.user_api.admin.accounting.service;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.common.entity.SearchDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface AdminAccountingService {

    Accounting getAccounting(Long id);
    List<Accounting> getAccountingList(SearchDto searchDto);
    void excelDown(SearchDto searchDto, HttpServletResponse response) throws IOException;
}
