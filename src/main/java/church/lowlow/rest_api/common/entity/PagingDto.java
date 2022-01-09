package church.lowlow.rest_api.common.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PagingDto {
    private int nowPage;
    private int totalPages;
}
