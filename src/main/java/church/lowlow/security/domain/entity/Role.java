package church.lowlow.security.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter @Setter @ToString(exclude = {"accounts","resourcesSet"})
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "id") @Builder
public class Role implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String roleName;

    private String roleDesc;

    private int roleNum;

    private boolean block; // 차단 유무

}