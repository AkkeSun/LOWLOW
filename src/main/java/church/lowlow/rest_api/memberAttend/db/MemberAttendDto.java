package church.lowlow.rest_api.memberAttend.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * MemberAttend 객채 검증을 위한 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAttendDto {

    private Integer memberId;
    private LocalDate checkDate;
    private boolean isAttend;
    private String note;

}
