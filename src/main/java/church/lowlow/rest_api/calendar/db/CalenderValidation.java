package church.lowlow.rest_api.calendar.db;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 종료일이 시작일보다 빠르면 에러 발생
 */
@Component
public class CalenderValidation implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CalendarDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CalendarDto dto = (CalendarDto) target;
        if(dto.getEndDate().before(dto.getStartDate()))
            errors.rejectValue("endDate", "wrongEndDate", "종료일은 시작일보다 빠를 수 없습니다");
    }
}
