package church.lowlow.rest_api.memberAttend.db;

import church.lowlow.rest_api.common.converter.LocalDateConverter;
import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Writer;
import church.lowlow.rest_api.member.db.Member;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * [ 성도 출석관리 ]
 *
 * @Column
 * id 고유 식별자
 * member 출석 체크 대상
 * checkDate 출석 체크 기준일
 * isAttend 출석 유무
 * writer 작성자
 * note 비고
 */
@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class MemberAttend extends BaseTimeEntity {

    @Id @GeneratedValue
    @ApiModelProperty(value = "고유 식별자", example = "79", required = true)
    private Integer id;

    @ManyToOne
    @ApiModelProperty(value = "성도 정보", required = true)
    private Member member;

    @Convert(converter = LocalDateConverter.class)
    @ApiModelProperty(value = "기준 체크일", example = "2022-05-19 04:16:54", required = true)
    private LocalDate checkDate;

    @ApiModelProperty(value = "출석 유무", example = "true", required = true)
    private boolean isAttend;

    @ApiModelProperty(value = "비고", example = "none")
    private String note;

    @Embedded
    @ApiParam(value = "작성자", required = true)
    private Writer writer;
}
