package church.lowlow.rest_api.weekly.db;

import church.lowlow.rest_api.common.entity.FileDto;
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

    private String title;
    private Writer writer;
    private String weeklyDate;

    private FileDto img1;
    private FileDto img2;
    private FileDto img3;
    private FileDto img4;

    private String img1_OriginalName;
    private String img2_OriginalName;
    private String img3_OriginalName;
    private String img4_OriginalName;
    private String img1_UploadName;
    private String img2_UploadName;
    private String img3_UploadName;
    private String img4_UploadName;
}
