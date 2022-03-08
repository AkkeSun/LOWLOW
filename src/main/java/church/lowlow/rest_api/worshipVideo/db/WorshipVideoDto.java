package church.lowlow.rest_api.worshipVideo.db;

import church.lowlow.rest_api.common.entity.Writer;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * WorshipVideo 객채 검증을 위한 DTO
 */
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class WorshipVideoDto{

    @NotBlank(message = "제목은 비워둘 수 없습니다")
    private String title;

    @NotBlank(message = "링크는 비워둘 수 없습니다")
    private String link;

    private Writer writer;
}
