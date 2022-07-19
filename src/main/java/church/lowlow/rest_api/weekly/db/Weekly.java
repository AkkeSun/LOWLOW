package church.lowlow.rest_api.weekly.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.common.entity.Writer;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.persistence.*;

/**
 * [ 주보 ]
 *
 * @Column
 * id 고유 식별자
 * title 제목
 * weeklyDate 주 차
 * writer 작성자
 * img 주보 이미지
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class Weekly extends BaseTimeEntity {

    @Id @GeneratedValue
    @ApiModelProperty(value = "고유 식별자", example = "79", required = true)
    private Integer id;
    
    @ApiModelProperty(value = "제목", example = "5월 19일 주보입니다", required = true)
    private String title;

    @ApiModelProperty(value = "주 차", example = "2022-05-19 04:16:54", required = true)
    private String weeklyDate;

    @Embedded
    @ApiParam(value = "작성자", required = true)
    private Writer writer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img1_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img1_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "img1_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "img1_fullUrl")),
    })
    @ApiParam(value = "주보 이미지 1")
    private FileDto img1;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img2_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img2_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "img2_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "img2_fullUrl")),
    })
    @ApiParam(value = "주보 이미지 2")
    private FileDto img2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img3_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img3_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "img3_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "img3_fullUrl")),
    })
    @ApiParam(value = "주보 이미지 3")
    private FileDto img3;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img4_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img4_UploadName")),
            @AttributeOverride(name = "fileDir",      column = @Column(name = "img4_fildDir")),
            @AttributeOverride(name = "fullUrl",      column = @Column(name = "img4_fullUrl")),
    })
    @ApiParam(value = "주보 이미지 4")
    private FileDto img4;
}
