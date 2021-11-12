package church.lowlow.rest_api.accounting.searchBox;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;


/**
 * 검색 시작일 종료일 확인
 */
@Component
public class SerchBoxValidation implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return SearchBox.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        SearchBox dto = (SearchBox) target;
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate   = dto.getEndDate();
        
        if(startDate == null && endDate != null)
            errors.rejectValue("startDate", "wrongStartDate", "시작일을 입력하지 않았습니다");
        else
            if(startDate != null && endDate == null)
            errors.rejectValue("endDate", "wrongEndDate", "종료일을 입력하지 않았습니다");
        else
            if(startDate != null && endDate != null && endDate.isBefore(startDate))
            errors.rejectValue("endDate", "wrongEndDate", "종료일은 시작일보다 빠를 수 없습니다");
    }
}
