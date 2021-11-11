package church.lowlow.rest_api.basicInfo.db;

import church.lowlow.rest_api.common.entity.Image;
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
    private Image infoImage1;
    private Image infoImage2;
    private Image infoImage3;
    private Image infoImage4;
    private Image infoImage5;
    private Image infoImage6;
    private Image carouselImg1;
    private Image carouselImg2;
    private Image carouselImg3;
    private Image carouselImg4;
    private Image carouselImg5;
    private Image carouselImg6;
    private Image organizationChart1;
    private Image organizationChart2;
    private Image organizationChart3;

}
