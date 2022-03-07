package church.lowlow.rest_api.weekly.db;

import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.db.MemberDto;
import church.lowlow.rest_api.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.regex.Pattern;

import static church.lowlow.rest_api.common.util.StringUtil.StringNullCheck;


/**
 * 주보 유효성 검사
 */
@Component
public class WeeklyValidation {

    public void validate(WeeklyDto dto, Errors errors){

        if(StringNullCheck(dto.getTitle()))
            errors.rejectValue("title", "wrongTitle", "제목은 비워둘 수 없습니다");
        else if(StringNullCheck(dto.getWeeklyDate()))
            errors.rejectValue("weeklyDate", "wrongWeeklyDate", "주차는 비워둘 수 없습니다");

    }
}
