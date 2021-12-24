package church.lowlow.rest_api.accounting.db;

import lombok.*;

/**
 * 헌금 종류별 총액을 구하기 위한 DTO
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsWithOfferingKind {

    private int TOTAL;
    private int SUNDAY;
    private int TITHE;
    private int THANKSGIVING;
    private int BUILDING;
    private int SPECIAL;
    private int MISSION;
    private int UNKNOWN;

}
