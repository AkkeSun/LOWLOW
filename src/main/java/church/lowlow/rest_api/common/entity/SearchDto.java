package church.lowlow.rest_api.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 검색 관련 DTO
 * searchId : 검색 카테고리
 * searchData : 검색어
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchDto {

    private String searchId;

    private String searchData;

}
