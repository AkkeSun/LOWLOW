package church.lowlow.rest_api.accounting.searchBox;

import church.lowlow.rest_api.common.entity.SearchDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;


/**
 * 검색 시작일 종료일 확인
 */
@Component
public class SerchBoxValidation  {

    public void dateValidate(SearchDto dto, Errors errors) {

        LocalDate startDate = dto.getStartDate();
        LocalDate endDate   = dto.getEndDate();

        if(startDate != null && endDate != null && endDate.isBefore(startDate))
        errors.rejectValue("endDate", "wrongEndDate", "종료일은 시작일보다 빠를 수 없습니다");
    }
}
