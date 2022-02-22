package church.lowlow.user_api.admin.accounting.service;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.accounting.db.OfferingKind;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.Member;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;

import static church.lowlow.user_api.admin.accounting.util.ExcelUtil.accountingExcelCreate;

@Service
public class DefaultAdminAccountingService implements AdminAccountingService{

    @Autowired
    private WebClient webClient;
    @Value("${fileUploadPath}")
    private String fileUploadPath;


    @Override
    public Accounting getAccounting(Long id) {
        Mono<Accounting> accountingMono = webClient
                .get()
                .uri("/accounting/{id}", id)
                .retrieve()
                .bodyToMono(Accounting.class);

        return accountingMono.block();
    }


    @Override
    public void excelDown(SearchDto searchDto, HttpServletResponse response) throws IOException {
        // 엑셀에 들어갈 데이터 로드
        List<Accounting> accountingList = getAccountingList(searchDto);
        Map<String, Object> statisticsMap = getStatisticsMap(searchDto);

        // 엑셀파일 생성
        accountingExcelCreate(searchDto, accountingList, statisticsMap);

        // 다운로드
        String fileName = "accounting.xlsx";

        byte[] fileByte = FileUtils.readFileToByteArray(new File(fileUploadPath + fileName));

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(LocalDate.now() + ".xlsx", "UTF-8")+"\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        response.getOutputStream().write(fileByte);
        response.getOutputStream().flush();
        response.getOutputStream().close();

    }





    // ============================ Accounting List 출력 함수 ========================
    @Override
    public List<Accounting> getAccountingList(SearchDto searchDto) {

        String searchId = searchDto.getSearchId();
        String searchData = searchDto.getSearchData();

        // Data Load
        Mono<Map> mono = webClient
                .get()
                .uri("/accounting?searchId="+searchId+"&searchData="+searchData
                        +"&nowPage=0&totalPages=9999")
                .retrieve()
                .bodyToMono(Map.class);


        // Data Convert
        Map<String, Object> resultMap = mono.block();
        ArrayList<Accounting> returnList = new ArrayList<>();
        LinkedHashMap<String, Object> lm = (LinkedHashMap<String, Object>) resultMap.get("_embedded");
        ArrayList<Map> accountingList = (ArrayList<Map>) lm.get("accountingList");

        for(Map accountingData : accountingList){

            HashMap<String, Object> memberMap = (HashMap<String, Object>) accountingData.get("member");

            Member member = null;
            if(memberMap != null){

                member = Member.builder()
                        .name((String)memberMap.get("name"))
                        .phoneNumber((String)memberMap.get("phoneNumber"))
                        .belong((String)memberMap.get("belong"))
                        .build();
            }

            Accounting accounting = Accounting.builder()
                    .money( (Integer) accountingData.get("money"))
                    .offeringKind( OfferingKind.valueOf ((String) accountingData.get("offeringKind")))
                    .offeringDate(LocalDate.parse((String) accountingData.get("offeringDate")))
                    .note((String) accountingData.get("note"))
                    .member(member)
                    .build();
            returnList.add(accounting);
        }

        return returnList;
    }



    // ======================== 헌금 내용 분석 출력함수========================
    public Map<String, Object> getStatisticsMap(SearchDto searchDto){

        String searchId = searchDto.getSearchId();
        String searchData = searchDto.getSearchData();

        // Data Load
        Mono<Map> mono = webClient
                .get()
                .uri("/accounting/statistics?searchId="+searchId+"&searchData="+searchData)
                .retrieve()
                .bodyToMono(Map.class);

        return mono.block();
    }

}
