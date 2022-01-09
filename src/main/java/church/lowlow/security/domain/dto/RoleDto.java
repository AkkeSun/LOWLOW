package church.lowlow.security.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * roleName : 이름 (ROLE_ADMIN)
 * roleDesc : 설명
 * roleNum  : 적용순서 = 낮을수록 순위가 높음
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto{

    private String roleName;
    private String roleDesc;
    private int roleNum;

}