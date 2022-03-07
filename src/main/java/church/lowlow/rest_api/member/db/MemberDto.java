package church.lowlow.rest_api.member.db;

import church.lowlow.rest_api.common.entity.FileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


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

    private FileDto image;

}
