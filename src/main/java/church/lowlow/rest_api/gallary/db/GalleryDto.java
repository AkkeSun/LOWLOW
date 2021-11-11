package church.lowlow.rest_api.gallary.db;

import church.lowlow.rest_api.common.entity.Image;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Gallery 객채 검증을 위한 DTO
 */
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class GalleryDto {

    @NotBlank(message = "제목은 비워둘 수 없습니다")
    private String title;

    @NotNull(message = "작성자는 비워둘 수 없습니다")
    private Writer writer;

    private Image image1;
    private Image image2;
    private Image image3;
    private Image image4;
    private Image image5;
    private Image image6;
}
