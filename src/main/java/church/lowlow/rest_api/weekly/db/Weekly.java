package church.lowlow.rest_api.weekly.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Files;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.*;

import javax.persistence.*;

/**
 * [ 주보 ]
 *
 * @Column
 * id 고유 식별자
 * title 제목
 * writer 작성자
 * img 주보 이미지
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class Weekly extends BaseTimeEntity {

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
    private Files img1;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img2_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img2_UploadName")),
    })
    private Files img2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img3_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img3_UploadName")),
    })
    private Files img3;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "originalName", column = @Column(name = "img4_OriginalName")),
            @AttributeOverride(name = "uploadName",   column = @Column(name = "img4_UploadName")),
    })
    private Files img4;
}
