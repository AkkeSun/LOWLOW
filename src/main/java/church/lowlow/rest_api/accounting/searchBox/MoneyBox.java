package church.lowlow.rest_api.accounting.searchBox;

import lombok.*;

/**
 * 헌금 종류별 금액 분류를 위한 DTO
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MoneyBox {

    private int TOTAL;
    private int SUNDAY;
    private int TITHE;
    private int THANKSGIVING;
    private int BUILDING;
    private int SPECIAL;
    private int MISSION;
    private int UNKNOWN;

}
