package church.lowlow.rest_api.accounting.searchBox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


/**
 * [회계 검색에 필요한 DTO]
 * 
 * KEY 목록
 * offeringKind(헌금종류)
 * name(이름)
 * belong(교구)
 * birthYear(또래)
 * churchOfficer(직분)
 */
@Data
@ToString
@NoArgsConstructor @AllArgsConstructor
public class SearchBox {

    private String key;
    private Object val;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
