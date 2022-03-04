package church.lowlow.rest_api.notice.db;

import church.lowlow.rest_api.common.entity.Writer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Notice 객채 검증을 위한 DTO
 */
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class NoticeDto {

    @NotBlank(message = "제목은 비워둘 수 없습니다")
    private String title;

    private Writer writer;
    
    @NotBlank(message = "내용은 비워둘 수 없습니다")
    private String contents;

}
