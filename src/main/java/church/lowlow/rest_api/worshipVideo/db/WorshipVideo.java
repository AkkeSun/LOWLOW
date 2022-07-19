package church.lowlow.rest_api.worshipVideo.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Writer;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.persistence.*;

/**
 * [ 예배영상 ]
 *
 * @Column
 * id 고유 식별자
 * title 제목
 * link 영상링크
 * writer 작성자
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class WorshipVideo extends BaseTimeEntity {

    @Id @GeneratedValue
    @ApiModelProperty(value = "고유 식별자", example = "79", required = true)
    private Integer id;
    
    @ApiModelProperty(value = "제목", example = "0월 0일 주일예배", required = true)
    private String title;

    @ApiModelProperty(value = "영상 링크", example = "www.youtube.com/..", required = true)
    private String link;

    @Embedded
    @ApiParam(value = "작성자", required = true)
    private Writer writer;

}
