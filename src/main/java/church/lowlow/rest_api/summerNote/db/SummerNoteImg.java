package church.lowlow.rest_api.summerNote.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * [ 서머노트 파일 업로드 entity ]
 *  image : 업로드 파일 이름
 *  writer : 작성자
 *  BbsType : 게시판 타입 (Gallery, Notice)
 */
@Entity @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class SummerNoteImg extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Integer id;
    
    @Embedded
    private FileDto image;

    @Embedded
    private Writer writer;

    private String bbsType;
}
