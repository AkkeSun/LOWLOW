package church.lowlow.security.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String passwordCheck;

    private String belong; // 교구 : 교구 리더인 경우 입력

    @NotNull
    private String role;

}