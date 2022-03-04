package church.lowlow.rest_api.basicInfo.db;

import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BasicInfo 객채 검증을 위한 DTO
 */
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class BasicInfoDto {
    private Integer id;
    private String basicInfo;
    private String detailInfo;
    private String name;
    private String senior_pastor_name;
    private String basicAddress;
    private String detailAddress;
    private String callNumber;
    private String kakaoPage;
    private String youtubeURL;
    private String instagram;
    private String blog;
    private Writer writer;
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
