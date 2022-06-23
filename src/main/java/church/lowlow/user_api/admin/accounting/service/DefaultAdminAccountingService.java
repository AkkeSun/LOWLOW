package church.lowlow.user_api.admin.accounting.service;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.user_api.admin.accounting.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class DefaultAdminAccountingService extends ExcelUtil implements AdminAccountingService {

    @Value("${restApiBaseUrl}")
    private String REST_API_URL;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional
    public Accounting getAccounting(Long id) {

        return restTemplate.exchange(REST_API_URL + "/accounting/" + id, HttpMethod.GET, null, Accounting.class).getBody();
    }


    @Override
    @Transactional
    public ArrayList<Map> getAccountingList(SearchDto searchDto) {

        String searchId = searchDto.getSearchId();
        String searchData = searchDto.getSearchData();
        String URL = REST_API_URL + "/accounting?searchId=" + searchId + "&searchData=" + searchData + "&nowPage=0&totalPages=9999";

        ResponseEntity<Map> resultMap
                = restTemplate.exchange(URL, HttpMethod.GET, null, Map.class);

        LinkedHashMap lm = (LinkedHashMap) resultMap.getBody().get("_embedded");

        return (ArrayList<Map>) lm.get("accountingList");
    }



    @Override
    @Transactional
    public Map<String, Object> getStatisticsMap(SearchDto searchDto){

        String searchId = searchDto.getSearchId();
        String searchData = searchDto.getSearchData();
        String URL = REST_API_URL + "/accounting/statistics?searchId="+searchId+"&searchData="+searchData;

        return restTemplate.exchange(URL, HttpMethod.GET, null, Map.class).getBody();
    }

}
