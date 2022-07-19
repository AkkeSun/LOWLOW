package church.lowlow.rest_api.calendar.db;

import church.lowlow.rest_api.common.converter.LocalDateConverter;
import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Writer;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

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
    @ApiModelProperty(value = "고유 식별자", example = "79", required = true)
    private Integer id;

    @Embedded
    @ApiParam(value = "작성자", required = true)
    private Writer writer;

    @ApiParam(value = "제목", required = true)
    private String title;

    @Convert(converter = LocalDateConverter.class)
    @ApiParam(value = "시작일", required = true)
    private LocalDate start;

    @Convert(converter = LocalDateConverter.class)
    @ApiParam(value = "종료일", required = true)
    private LocalDate end;

}
