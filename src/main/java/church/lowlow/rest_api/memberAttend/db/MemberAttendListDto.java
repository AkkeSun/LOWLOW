package church.lowlow.rest_api.memberAttend.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAttendListDto {

    private LocalDate checkDate;
    private Long isAttendTrue;
    private Long isAttendFalse;
    private Long total;

}
