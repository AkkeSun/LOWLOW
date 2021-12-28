package church.lowlow.user_api.admin;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.accounting.db.AccountingDto;
import church.lowlow.rest_api.accounting.db.OfferingKind;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.ChurchOfficer;
import church.lowlow.rest_api.member.db.Member;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;

import static church.lowlow.user_api.common.ExcelUtil.accountingExcelCreate;

@Controller
@RequestMapping("/admin/accounting")
public class AdminAccountingController {

    @Autowired
    private WebClient webClient;

    @Autowired
    private ModelMapper modelMapper;

    // ========== List View ==========
    @GetMapping
    public String getAccountingListView() {
        return "admin/accounting/accountingList";
    }


    // ========== Create View ==========
    @GetMapping("/create")
    public String getAccountingCreateView(Model model) {
        model.addAttribute("accounting", new Accounting());
        return "admin/accounting/accountingCreate";
    }


    // ========== Detail (Update) View ==========
    @GetMapping("/{id}")
    public String getAccuntingCreateView(@PathVariable Long id, Model model) {

        Mono<Member> memberMono = webClient
                .get()
                .uri("/members/{id}", id)
                .retrieve()
                .bodyToMono(Member.class);

        Member member = memberMono.block();
        model.addAttribute("member", member);
        return "admin/member/memberDetail";

    }


    // =================== 엑셀파일 다운로드 =====================
    @GetMapping("/excelDown")
    public void download(SearchDto searchDto, HttpServletResponse response) throws IOException {

        // 엑셀에 들어갈 데이터 로드
        List<Accounting> accountingList = getAccountList(searchDto);
        Map<String, Object> statisticsMap = getStatisticsMap(searchDto);

        // 엑셀파일 생성
        accountingExcelCreate(searchDto, accountingList, statisticsMap);

        // 다운로드
        String filePath = "C:/upload/";
        String fileName = "accounting.xlsx";

        byte[] fileByte = FileUtils.readFileToByteArray(new File(filePath + fileName));

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(LocalDate.now() + ".xlsx", "UTF-8")+"\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        response.getOutputStream().write(fileByte);
        response.getOutputStream().flush();
        response.getOutputStream().close();

    }



    // =================== Accounting List 출력 함수  =====================
    public List getAccountList(SearchDto searchDto){

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
