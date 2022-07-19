package church.lowlow.rest_api.gallery.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.common.entity.Writer;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
@DynamicUpdate
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class Gallery extends BaseTimeEntity {

    @Id @GeneratedValue
    @ApiModelProperty(value = "고유 식별자", example = "79", required = true)
    private Integer id;

    @ApiModelProperty(value = "제목", example = "갤러리 입니다", required = true)
    private String title;
    
    @Embedded
    @ApiParam(value = "작성자", required = true)
    private Writer writer;

    @Column(columnDefinition = "LONGTEXT")
    @ApiModelProperty(value = "내용", example = "갤러리 내용입니다", required = true)
    private String contents;

}
