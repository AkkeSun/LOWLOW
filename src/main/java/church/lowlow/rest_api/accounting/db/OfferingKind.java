package church.lowlow.rest_api.accounting.db;

/**
 * 헌금 종류
 * 
 * SUNDAY 주정
 * TITHE 십일조
 * THANKSGIVING 감사
 * BUILDING 건축
 * SPECIAL 특별
 * MISSION 선교
 * UNKNOWN 기타
 * TOTAL   전체 : 종류별 총액을 계산하기 위한 값
 */
public enum OfferingKind {
    SUNDAY, TITHE, THANKSGIVING, BUILDING, SPECIAL, MISSION, UNKNOWN, TOTAL
}
