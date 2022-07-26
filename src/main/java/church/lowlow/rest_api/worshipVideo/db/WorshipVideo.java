package church.lowlow.rest_api.worshipVideo.db;

import church.lowlow.rest_api.common.entity.BaseTimeEntity;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * [ 예배영상 ]
 *
 * @Column
 * id 고유 식별자
 * title 제목
 * link 영상링크
 * writer 작성자
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString @EqualsAndHashCode(of = "id")
public class WorshipVideo extends BaseTimeEntity {

    @Id @GeneratedValue
    private Integer id;
    
    private String title;

    private String link;

    @Embedded
    private Writer writer;

}
