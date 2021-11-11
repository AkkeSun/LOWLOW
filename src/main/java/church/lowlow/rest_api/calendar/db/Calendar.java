package church.lowlow.rest_api.calendar.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * [ Calendar ]
 *
 * @Column
 * id 고유 식별자
 * content 내용
 * startDate 시작일
 * endDate 종료일
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class Calendar extends BaseTimeEntity {

    @Id @GeneratedValue
    private Integer id;

    @Embedded
    private Writer writer;

    private String content;

    private Date startDate;

    private Date endDate;

}
