package church.lowlow.rest_api.accounting.db;
import church.lowlow.rest_api.common.entity.Writer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Accounting 객채 검증을 위한 DTO
 * 익명인 경우 name="익명"
 */
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class AccountingDto {

    private Integer memberId;

    private int money;

    private OfferingKind offeringKind;

    private LocalDate offeringDate;

    private Writer writer;

    private String note;

}
