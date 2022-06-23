package church.lowlow.user_api.admin.accounting.service;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.accounting.db.AccountingDto;
import church.lowlow.rest_api.common.entity.SearchDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface AdminAccountingService {

    Accounting getAccounting(Long id);
    ArrayList <Map> getAccountingList(SearchDto searchDto);
    Map<String, Object> getStatisticsMap(SearchDto searchDto);

}
