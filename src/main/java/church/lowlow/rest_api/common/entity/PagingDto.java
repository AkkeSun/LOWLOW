package church.lowlow.rest_api.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PagingDto {
    @ApiModelProperty(value = "현재 페이지")
    private int nowPage;
    
    @ApiModelProperty(value = "전체 페이지")
    private int totalPages;
}
