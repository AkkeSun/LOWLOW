package church.lowlow.rest_api.basicInfo.db;

import church.lowlow.rest_api.common.entity.FileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;

/**
 * BasicInfo 객채 검증을 위한 DTO
 */
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class BasicInfoDto {

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

    private String Info1_OriginalName;
    private String Info1_UploadName;
    private String Info2_OriginalName;
    private String Info2_UploadName;
    private String Info3_OriginalName;
    private String Info3_UploadName;
    private String Info4_OriginalName;
    private String Info4_UploadName;
    private String Info5_OriginalName;
    private String Info5_UploadName;
    private String Info6_OriginalName;
    private String Info6_UploadName;

    private String Car1_OriginalName;
    private String Car1_UploadName;
    private String Car2_OriginalName;
    private String Car2_UploadName;
    private String Car3_OriginalName;
    private String Car3_UploadName;
    private String Car4_OriginalName;
    private String Car4_UploadName;
    private String Car5_OriginalName;
    private String Car5_UploadName;
    private String Car6_OriginalName;
    private String Car6_UploadName;
    private String Chart1_OriginalName;
    private String Chart1_UploadName;
    private String Chart2_OriginalName;
    private String Chart2_UploadName;
    private String Chart3_OriginalName;
    private String Chart3_UploadName;

}
