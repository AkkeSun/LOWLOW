package church.lowlow.rest_api.accounting.db;

import church.lowlow.rest_api.common.converter.LocalDateConverter;
import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Writer;
import church.lowlow.rest_api.member.db.Member;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
    @ApiModelProperty(value = "고유 식별자", example = "79", required = true)
    private Integer id;

    @ManyToOne
    @ApiModelProperty(value = "헌금한 성도에 대한 정보", required = true)
    private Member member;

    @ApiModelProperty(value = "헌금 액수", example = "10000", required = true)
    private int money;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "헌금 종류", example = "SUNDAY", required = true)
    private OfferingKind offeringKind;

    @ApiParam(value = "헌금한 날짜", example = "2022-05-17 15:00:00", required = true)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate offeringDate;

    @ApiParam(value = "비고", example = "none")
    private String note;

    @Embedded
    @ApiParam(value = "작성자", required = true)
    private Writer writer;

}
