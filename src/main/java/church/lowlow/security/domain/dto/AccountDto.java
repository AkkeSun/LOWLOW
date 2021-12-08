package church.lowlow.security.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private String role;
}