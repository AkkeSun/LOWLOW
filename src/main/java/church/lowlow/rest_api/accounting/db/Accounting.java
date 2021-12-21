package church.lowlow.rest_api.accounting.db;

import church.lowlow.rest_api.common.converter.LocalDateConverter;
import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Writer;
import church.lowlow.rest_api.member.db.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * [ 헌금 회계관리 ]
 *
 * @Column
 * id 고유 식별자
 * member 헌금한 사람
 * money 금액
 * offeringKind 종류
 * offeringDate 헌금일
 * note 비고
 * writer 작성자
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class Accounting extends BaseTimeEntity {

    @Id @GeneratedValue
    private Integer id;

    @ManyToOne
    private Member member;

    private int money;

    @Enumerated(EnumType.STRING)
    private OfferingKind offeringKind;

    @Convert(converter = LocalDateConverter.class)
    private LocalDate offeringDate;

    private String note;

    @Embedded
    private Writer writer;

}
