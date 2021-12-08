package church.lowlow.rest_api.member.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Image;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Date;


/**
 * Member 객채 검증을 위한 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @NotBlank(message = "이름은 비워둘 수 없습니다")
    private String name;

    @Pattern(regexp="\\d{3}-\\d{4}-\\d{4}", message = "올바른 전화번호가 아닙니다")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "교구는 비워둘 수 없습니다")
    private String belong;

    @NotNull(message = "등록일은 비워둘 수 없습니다")
    private LocalDate regiDate;

    @Min(value=1, message = "또래(생년)는 비워둘수 없습니다")
    private int birthYear;

    @NotNull(message = "성별은 비워둘 수 없습니다")
    private Gender gender;

    @NotNull(message = "직분은 비워둘 수 없습니다")
    private ChurchOfficer churchOfficer;

    @NotNull(message = "작성자는 비워둘 수 없습니다")
    private Writer writer;

    private Image image;

}
