package church.lowlow.rest_api.basicInfo.db;

import church.lowlow.rest_api.common.entity.FileDto;
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
    private FileDto infoImage1;
    private FileDto infoImage2;
    private FileDto infoImage3;
    private FileDto infoImage4;
    private FileDto infoImage5;
    private FileDto infoImage6;
    private FileDto carouselImg1;
    private FileDto carouselImg2;
    private FileDto carouselImg3;
    private FileDto carouselImg4;
    private FileDto carouselImg5;
    private FileDto carouselImg6;
    private FileDto organizationChart1;
    private FileDto organizationChart2;
    private FileDto organizationChart3;

}
