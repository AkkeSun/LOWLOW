package church.lowlow.rest_api.summerNote.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummerNoteImgDto {

    private String uploadName;
    private String originalName;
    private String bbsType;

}
