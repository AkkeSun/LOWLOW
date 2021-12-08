package church.lowlow.security.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter @Setter @ToString @EqualsAndHashCode(of = "id")
@AllArgsConstructor  @NoArgsConstructor @Builder
public class Account {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private String password;

    @JsonManagedReference // 순환참조 방지 (부모 엔티티에 붙이기)
    @ManyToOne(fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    private Role userRole;

}