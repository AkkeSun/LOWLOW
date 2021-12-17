package church.lowlow.rest_api.member.db;

import church.lowlow.rest_api.common.converter.LocalDateConverter;
import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Files;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * [ 교인 정보 ]
 *
 * @Column
 * id 고유 식별자
 * name 이름
 * phoneNumber 전화번호
 * address 주소
 * belong 교구
 * birthYear 또래 (91)
 * regiDate 가입일
 * gender 성별
 * churchOfficer 직분
 * image 이미지
 * writer 작성자
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder @ToString @EqualsAndHashCode(of = "id")
public class Member extends BaseTimeEntity{

    @Id @GeneratedValue
    private Integer id;

    private String name;

    private String phoneNumber;

    private String belong;

    private int birthYear;

    @Convert(converter = LocalDateConverter.class)
    private LocalDate regiDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private ChurchOfficer churchOfficer;

    @Embedded
    private Files image;

    @Embedded
    private Writer writer;

}
