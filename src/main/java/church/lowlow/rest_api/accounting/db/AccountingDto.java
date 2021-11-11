package church.lowlow.rest_api.accounting.db;
import church.lowlow.rest_api.common.entity.Writer;
import church.lowlow.rest_api.member.db.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

/**
 * Accounting 객채 검증을 위한 DTO
 */
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class AccountingDto {

    @NotBlank(message = "이름을 입력하세요")
    private String name;

    @NotNull(message = "금액은 입력하세요")
    private int money;

    @NotNull(message = "헌금 종류를 선택하세요")
    private OfferingKind offeringKind;

    @NotNull(message = "헌금일을 작성하세요")
    private Date offeringDate;

    @NotNull(message = "작성자는 비워둘 수 없습니다")
    private Writer writer;

    private String note;



}
