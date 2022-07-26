package church.lowlow.rest_api.calendar.db;

import church.lowlow.rest_api.common.converter.LocalDateConverter;
import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * [ Calendar ]
 *
 * @Column
 * id 고유 식별자
 * title 내용
 * start 시작일
 * end 종료일
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class Calendar extends BaseTimeEntity {

    @Id @GeneratedValue
    private Integer id;

    @Embedded
    private Writer writer;

    private String title;

    @Convert(converter = LocalDateConverter.class)
    private LocalDate start;

    @Convert(converter = LocalDateConverter.class)
    private LocalDate end;

}
