package church.lowlow.rest_api.notice.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Writer;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
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
