package church.lowlow.rest_api.gallary.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Files;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.*;

import javax.persistence.*;

/**
 * [ 갤러리 ]
 *
 * @Column
 * id 고유 식별자
 * title 제목
 * img 이미지 (최대 6개)
 * writer 작성자
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class Gallery extends BaseTimeEntity {

    @Id @GeneratedValue
    private Integer id;

    private String title;
    
    @Embedded
    private Writer writer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img1_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img1_UploadName")),
    })
    private Files image1;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img2_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img2_UploadName")),
    })
    private Files image2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img3_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img3_UploadName")),
    })
    private Files image3;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img4_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img4_UploadName")),
    })
    private Files image4;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img5_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img5_UploadName")),
    })
    private Files image5;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img6_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img6_UploadName")),
    })
    private Files image6;

}
