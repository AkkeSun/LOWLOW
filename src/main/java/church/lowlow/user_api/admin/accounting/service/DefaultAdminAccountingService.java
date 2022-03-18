package church.lowlow.user_api.admin.accounting.service;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.user_api.admin.accounting.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class DefaultAdminAccountingService extends ExcelUtil implements AdminAccountingService {

    @Autowired
    private WebClient webClient;

    @Override
    @Transactional
    public Accounting getAccounting(Long id) {
        Mono<Accounting> accountingMono = webClient
                .get()
                .uri("/accounting/{id}", id)
                .retrieve()
                .bodyToMono(Accounting.class);

        return accountingMono.block();
    }



    @Override
    @Transactional
    public List<LinkedHashMap<String, Object>> getAccountingList(SearchDto searchDto) {

        String searchId = searchDto.getSearchId();
        String searchData = searchDto.getSearchData();

        Mono<ResponseEntity<Object>> mono = webClient
                .get()
                .uri("/accounting?searchId=" + searchId + "&searchData=" + searchData
                        + "&nowPage=0&totalPages=9999")
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(Object.class));

        ResponseEntity<Object> block = mono.block();
        Map<String, Object> body = (Map<String, Object>) block.getBody();
        List<LinkedHashMap<String, Object>> accountingList = (List<LinkedHashMap<String, Object>>) ((Map<String, Object>) body.get("_embedded")).get("accountingList");

        return accountingList;
    }



    @Override
    @Transactional
    public Map<String, Object> getStatisticsMap(SearchDto searchDto){

        String searchId = searchDto.getSearchId();
        String searchData = searchDto.getSearchData();

        Mono<Map> mono = webClient
                .get()
                .uri("/accounting/statistics?searchId="+searchId+"&searchData="+searchData)
                .retrieve()
                .bodyToMono(Map.class);

        return mono.block();
    }

}
