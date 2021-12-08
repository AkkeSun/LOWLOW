package church.lowlow.security.domain.dto;

import lombok.*;

/**
 * resourceType : url or method
 * resourceName : url = path (/test/**)
 *              : method = package.class.method (church.lowlow.api.service.testMethod)
 * orderNum     : 적용순서 = 낮을수록 순위가 높음
 * roleName     : 접근 가능 Role
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResourcesDto {

    private String resourceType;
    private String resourceName;
    private int orderNum;
    private String roleName;

}