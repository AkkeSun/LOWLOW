package church.lowlow.rest_api.memberAttend.db;

import church.lowlow.rest_api.common.converter.LocalDateConverter;
import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Writer;
import church.lowlow.rest_api.member.db.Member;
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
    private Integer id;

    @ManyToOne
    private Member member;

    @Convert(converter = LocalDateConverter.class)
    private LocalDate checkDate;

    private boolean isAttend;

    private String note;

    @Embedded
    private Writer writer;
}
