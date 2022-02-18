package church.lowlow.security.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter @Setter @ToString(exclude = {"roleSet"}) @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder
@NamedEntityGraph(name = "getRole", attributeNodes = @NamedAttributeNode("resourceRole")) // 자식 엔티티를 무조건 얻지 않기 위한 설정
public class Resources implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String resourceType;

    private String resourceName;

    private boolean block; // 차단 유무

    private int orderNum;

    @JsonBackReference // 순환참조 방지 (부모엔티티에 붙이기)
    @ManyToOne
    private Role resourceRole;
}