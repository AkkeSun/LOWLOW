package church.lowlow.rest_api.accounting.db;


import lombok.*;

/**
 * 헌금종류별 헌금액수를 출력하기 위한 DTO
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class OfferingKindMoneyCountDto {

    private String offeringKind;
    private int money;

}
