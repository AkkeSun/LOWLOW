package church.lowlow.rest_api.accounting.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Writer;
import church.lowlow.rest_api.member.db.Member;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * [ 헌금 회계관리 ]
 *
 * @Column
 * id 고유 식별자
 * person 이름
 * money 금액
 * offeringKind 종류
 * offeringDate 헌금일
 * note 비고
 * writer 작성자
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
@NamedEntityGraph(name = "personGet", attributeNodes = @NamedAttributeNode("person"))
public class Accounting extends BaseTimeEntity {

    @Id @GeneratedValue
    private Integer id;

    @ManyToOne
    private Member person;

    private int money;

    private OfferingKind offeringKind;
    
    private Date offeringDate;

    private String note;

    @Embedded
    private Writer writer;
}
