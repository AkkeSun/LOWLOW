package church.lowlow.rest_api.member.db;

import church.lowlow.rest_api.common.entity.Files;
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


/**
 * Member 객채 검증을 위한 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private String name;

    private String phoneNumber;

    private LocalDate birthDay;

    private Gender gender;

    private String belong;

    private ChurchOfficer churchOfficer;

    private LocalDate regiDate;

    private String originalName;

    private String uploadName;

}
