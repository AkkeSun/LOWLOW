package church.lowlow.rest_api.notice.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.*;

import javax.persistence.*;

/**
 * [ 공지사항 ]
 *
 * @Column
 * id 고유 식별자
 * title 제목
 * content 내용
 * img 이미지 (최대 6개)
 * writer 작성자
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class Notice extends BaseTimeEntity {

    @Id @GeneratedValue
    private Integer id;

    private String title;

    private String content;

    @Embedded
    private Writer writer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img1_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img1_UploadName")),
    })
    private FileDto image1;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img2_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img2_UploadName")),
    })
    private FileDto image2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img3_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img3_UploadName")),
    })
    private FileDto image3;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img4_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img4_UploadName")),
    })
    private FileDto image4;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img5_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img5_UploadName")),
    })
    private FileDto image5;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img6_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img6_UploadName")),
    })
    private FileDto image6;
}
