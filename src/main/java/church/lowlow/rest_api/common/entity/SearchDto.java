package church.lowlow.rest_api.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 검색 관련 DTO
 * searchId : 검색 카테고리
 * searchData : 검색어
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SearchDto {

    @ApiModelProperty(value = "검색 아이디", example = "3")
    private String searchId;

    @ApiModelProperty(value = "검색 날짜")
    private String searchData;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "검색 날짜(시작일)")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "검색 날짜(종료일)")
    private LocalDate endDate;

}
