package church.lowlow.rest_api.basicInfo.db;

import church.lowlow.rest_api.common.entity.Files;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * BasicInfo 객채 검증을 위한 DTO
 */
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class BasicInfoDto {

    @NotBlank(message = "소개글은 비워둘 수 없습니다")
    private String info;

    @NotBlank(message = "교회 이름은 비워둘 수 없습니다")
    private String name;

    @NotBlank(message = "담임목사 이름은 비워둘 수 없습니다")
    private String senior_pastor_name;

    @NotBlank(message = "주소는 비워둘 수 없습니다")
    private String address;

    @NotBlank(message = "전화번호는 비워둘 수 없습니다")
    private String callNumber;

    @NotNull(message = "작성자는 비워둘 수 없습니다")
    private Writer writer;

    private String kakaoPage;
    private String liveWorshipURL;
    private Files infoImage1;
    private Files infoImage2;
    private Files infoImage3;
    private Files infoImage4;
    private Files infoImage5;
    private Files infoImage6;
    private Files carouselImg1;
    private Files carouselImg2;
    private Files carouselImg3;
    private Files carouselImg4;
    private Files carouselImg5;
    private Files carouselImg6;
    private Files organizationChart1;
    private Files organizationChart2;
    private Files organizationChart3;

}
