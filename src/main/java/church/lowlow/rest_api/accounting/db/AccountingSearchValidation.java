package church.lowlow.rest_api.accounting.db;

import church.lowlow.rest_api.common.entity.SearchDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDate;


/**
 * 검색 시작일 종료일 확인
 */
@Component
public class AccountingSearchValidation {

    public void dateValidate(SearchDto dto, Errors errors) {

        LocalDate startDate = dto.getStartDate();
        LocalDate endDate   = dto.getEndDate();

        if(startDate == null && endDate != null)
            errors.rejectValue(null, "wrongStartDate", "시작일을 입력하지 않았습니다");

        if(startDate != null &&  LocalDate.now().isBefore(startDate))
            errors.rejectValue(null, "wrongStartDate", "시작일은 현재시간보다 빠를 수 없습니다");

        if(startDate != null && endDate != null && endDate.isBefore(startDate))
            errors.rejectValue(null, "wrongEndDate", "종료일은 시작일보다 빠를 수 없습니다");

    }
}
