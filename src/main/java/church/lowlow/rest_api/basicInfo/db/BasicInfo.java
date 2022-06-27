package church.lowlow.rest_api.basicInfo.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.common.entity.Writer;
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
    private Integer id;

    private String name;

    private String senior_pastor_name;

    private String callNumber;

    @Column(columnDefinition = "LONGTEXT")
    private String basicInfo;
    
    private String detailInfo;

    private String basicAddress;

    private String detailAddress;

    private String kakaoPage;

    private String youtubeURL;

    private String instagram;

    private String blog;

    @Embedded
    private Writer writer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Info1_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Info1_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Info1_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Info1_fullUrl")),
    })
    private FileDto infoImage1;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Info2_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Info2_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Info2_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Info2_fullUrl")),
    })
    private FileDto infoImage2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Info3_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Info3_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Info3_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Info3_fullUrl")),
    })
    private FileDto infoImage3;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Info4_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Info4_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Info4_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Info4_fullUrl")),
    })
    private FileDto infoImage4;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Info5_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Info5_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Info5_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Info5_fullUrl")),
    })
    private FileDto infoImage5;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Info6_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Info6_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Info6_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Info6_fullUrl")),
    })
    private FileDto infoImage6;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Car1_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Car1_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Car1_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Car1_fullUrl")),
    })
    private FileDto carouselImg1;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Car2_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Car2_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Car2_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Car2_fullUrl")),
    })
    private FileDto carouselImg2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Car3_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Car3_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Car3_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Car3_fullUrl")),
    })
    private FileDto carouselImg3;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Car4_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Car4_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Car4_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Car4_fullUrl")),
    })
    private FileDto carouselImg4;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Car5_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Car5_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Car5_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Car5_fullUrl")),
    })
    private FileDto carouselImg5;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Car6_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Car6_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Car6_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Car6_fullUrl")),
    })
    private FileDto carouselImg6;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Chart1_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Chart1_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Chart1_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Chart1_fullUrl")),
    })
    private FileDto organizationChart1;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Chart2_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Chart2_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Chart2_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Chart2_fullUrl")),
    })
    private FileDto organizationChart2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "Chart3_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "Chart3_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "Chart3_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "Chart3_fullUrl")),
    })
    private FileDto organizationChart3;
}
