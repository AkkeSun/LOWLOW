package church.lowlow.rest_api.weekly.db;

import church.lowlow.rest_api.common.entity.Files;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Weekly 객채 검증을 위한 DTO
 */
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class WeeklyDto {

    @NotBlank(message = "제목은 비워둘 수 없습니다")
    private String title;

    @NotNull(message = "작성자는 비워둘 수 없습니다")
    private Writer writer;

    private Files img1;
    private Files img2;
    private Files img3;
    private Files img4;
}
