package church.lowlow.rest_api.calendar.db;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Notice 객채 검증을 위한 DTO
 */
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class CalendarDto {

    @NotBlank(message = "내용을 입력하세요")
    private String content;

    @NotNull(message = "작성자는 비워둘 수 없습니다")
    private Writer writer;

    @NotNull(message = "시작일을 선택하세요")
    private Date startDate;
    
    @NotNull(message = "종료일을 선택하세요")
    private Date endDate;
}
