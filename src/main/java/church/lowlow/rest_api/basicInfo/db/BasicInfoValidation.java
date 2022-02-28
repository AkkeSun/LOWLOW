package church.lowlow.rest_api.basicInfo.db;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;


import static church.lowlow.rest_api.common.util.StringUtil.StringNullCheck;


@Component
public class BasicInfoValidation {

    public void chapter1Validate(BasicInfoDto dto, Errors errors) {
        if(StringNullCheck(dto.getName()))
            errors.rejectValue("name", "wrongName", "교회 이름은 비워둘 수 없습니다");

        if(StringNullCheck(dto.getSenior_pastor_name()))
            errors.rejectValue("senior_pastor_name", "wrongSenior_pastor_name", "담임목사 이름은 비워둘 수 없습니다");

        if(StringNullCheck(dto.getBasicAddress()))
            errors.rejectValue("basicAddress", "wrongBasicAddress", "주소는 비워둘 수 없습니다");

        if(StringNullCheck(dto.getCallNumber()))
            errors.rejectValue("callNumber", "wrongCallNumber", "전화번호는 비워둘 수 없습니다");
    }

    public void chapter2Validate(BasicInfoDto dto, Errors errors) {
        if (StringNullCheck(dto.getBasicInfo()))
            errors.rejectValue("basicInfo", "wrongBasicInfo", "기본 소개글은 비워둘 수 없습니다");

        if (StringNullCheck(dto.getDetailInfo()))
            errors.rejectValue("detailInfo", "wrongDetailInfo", "상세 소개글은 비워둘 수 없습니다");
    }
}
