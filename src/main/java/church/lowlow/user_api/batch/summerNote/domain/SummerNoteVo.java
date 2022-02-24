package church.lowlow.user_api.batch.summerNote.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 배치작업을 위한 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummerNoteVo {

    private String uploadName;
    private String bbsType;
    private Integer id;

}
