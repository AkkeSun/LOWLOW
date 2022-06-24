package church.lowlow.rest_api.common.entity;

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

    private String searchId;

    private String searchData;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}
