package church.lowlow.rest_api.basicInfo.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.common.entity.Writer;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.persistence.*;


/**
 * [ 교회 기본정보 ]
 * 메인 페이지 및 교회소개 데이터
 *
 * @Column
 * [chapter1]
 * id 고유 식별자
 * name 교회 이름
 * senior_pastor_name 담임목사 이름
 * callNumber 교회 전화번호
 * basicAddress 기본 주소
 * basicAddress 상세 주소
 * youtubeURL 온라인 예배 링크
 * kakaoPage 카카오페이지 링크
 * instagram 인스타그램 링크
 * blog 블로그 링크
 *
 * [chapter2]
 * basicInfo 메인 페이지에 노출되는 한 문장의 기본 소개글
 * detailInfo 상세 소개글
 *
 * [chapter3]
 * infoImage 교회 소개글 이미지 (최대 6개)
 * carouselImg 메인 carousel 이미지 (최대 6개)
 * organizationChart 섬기는 사람들 이미지 (최대 3개)
 *
 * writer 작성자
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class BasicInfo extends BaseTimeEntity {

    @Id @GeneratedValue
    @ApiModelProperty(value = "고유 식별자", example = "79", required = true)
    private Integer id;
    
    @ApiModelProperty(value = "교회 이름", example = "위례세움교회", required = true)
    private String name;

    @ApiModelProperty(value = "목사님 성함", example = "최성광", required = true)
    private String senior_pastor_name;

    @ApiModelProperty(value = "교회 전화번호", example = "010-1234-1234", required = true)
    private String callNumber;

    @ApiModelProperty(value = "교회 기본 소개글", example = "착하고 충성된 교회", required = true)
    private String basicInfo;

    @Column(columnDefinition = "LONGTEXT")
    @ApiModelProperty(value = "교회 상세 소개글", example = "위례세움교회는 2018년에 세워진 하나님이 사랑하시는 교회로...", required = true)
    private String detailInfo;

    @ApiModelProperty(value = "기본 주소", example = "서울 특별시 은평구 갈현2동", required = true)
    private String basicAddress;

    @ApiModelProperty(value = "상세 주소", example = "101동 101호", required = true)
    private String detailAddress;

    @ApiModelProperty(value = "카카오 페이지", example = "myKakaoId")
    private String kakaoPage;
    
    @ApiModelProperty(value = "유투브 주소", example = "www.youtube.com/...")
    private String youtubeURL;
    
    @ApiModelProperty(value = "인스타그램 아이디", example = "myInstaId")
    private String instagram;

    @ApiModelProperty(value = "블로그 주소", example = "www.naver.com/blog/...")
    private String blog;

    @Embedded
    @ApiParam(value = "작성자", required = true)
    private Writer writer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Info1_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Info1_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Info1_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Info1_fullUrl")),
    })
    @ApiParam(value = "기본 정보 이미지 1")
    private FileDto infoImage1;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Info2_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Info2_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Info2_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Info2_fullUrl")),
    })
    @ApiParam(value = "기본 정보 이미지 2")
    private FileDto infoImage2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Info3_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Info3_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Info3_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Info3_fullUrl")),
    })
    @ApiParam(value = "기본 정보 이미지 3")
    private FileDto infoImage3;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Info4_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Info4_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Info4_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Info4_fullUrl")),
    })
    @ApiParam(value = "기본 정보 이미지 4")
    private FileDto infoImage4;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Info5_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Info5_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Info5_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Info5_fullUrl")),
    })
    @ApiParam(value = "기본 정보 이미지 5")
    private FileDto infoImage5;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Info6_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Info6_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Info6_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Info6_fullUrl")),
    })
    @ApiParam(value = "기본 정보 이미지 6")
    private FileDto infoImage6;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Car1_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Car1_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Car1_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Car1_fullUrl")),
    })
    @ApiParam(value = "캐러셋 이미지 1")
    private FileDto carouselImg1;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Car2_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Car2_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Car2_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Car2_fullUrl")),
    })
    @ApiParam(value = "캐러셋 이미지 2")
    private FileDto carouselImg2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Car3_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Car3_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Car3_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Car3_fullUrl")),
    })
    @ApiParam(value = "캐러셋 이미지 3")
    private FileDto carouselImg3;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Car4_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Car4_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Car4_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Car4_fullUrl")),
    })
    @ApiParam(value = "캐러셋 이미지 4")
    private FileDto carouselImg4;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Car5_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Car5_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Car5_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Car5_fullUrl")),
    })
    @ApiParam(value = "캐러셋 이미지 5")
    private FileDto carouselImg5;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Car6_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Car6_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Car6_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Car6_fullUrl")),
    })
    @ApiParam(value = "캐러셋 이미지 6")
    private FileDto carouselImg6;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Chart1_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Chart1_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Chart1_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Chart1_fullUrl")),
    })
    @ApiParam(value = "섬기는 사람들 이미지 1")
    private FileDto organizationChart1;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Chart2_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Chart2_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Chart2_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Chart2_fullUrl")),
    })
    @ApiParam(value = "섬기는 사람들 이미지 2")
    private FileDto organizationChart2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Chart3_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Chart3_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Chart3_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Chart3_fullUrl")),
    })
    @ApiParam(value = "섬기는 사람들 이미지 3")
    private FileDto organizationChart3;
}
