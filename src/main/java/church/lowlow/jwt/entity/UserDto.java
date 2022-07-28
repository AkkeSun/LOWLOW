package church.lowlow.jwt.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;
    private String email;
    private String password;
    private String role;
    private String refreshToken;

}
