package church.lowlow.rest_api.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingDto {

    private int nowPage;
    private int totalPages;
}
