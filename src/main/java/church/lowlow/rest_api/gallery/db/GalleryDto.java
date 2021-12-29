package church.lowlow.rest_api.gallery.db;

import church.lowlow.rest_api.common.entity.FileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Gallery 객채 검증을 위한 DTO
 */
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class GalleryDto {

    @NotBlank(message = "제목은 비워둘 수 없습니다")
    private String title;

    private FileDto image1;
    private FileDto image2;
    private FileDto image3;
    private FileDto image4;
    private FileDto image5;
    private FileDto image6;
}
