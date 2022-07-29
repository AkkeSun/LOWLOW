package church.lowlow.jwt.validation;

import church.lowlow.jwt.entity.SecurityDto;
import church.lowlow.jwt.entity.TokenDto;
import church.lowlow.jwt.entity.User;
import church.lowlow.jwt.repository.UserRepository;
import church.lowlow.jwt.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class TokenValidation {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository repository;

    public SecurityDto duplicated(TokenDto token) {

        Optional<User> byRefreshToken = repository.findByRefreshToken(token.getRefreshToken());

        // STEP 1 : 유저가 보낸 refresh 토큰과 db에 저장된 refresh 토큰이 같은지 확인
        if(byRefreshToken.isEmpty())
            return SecurityDto.builder().errCode("wrongRefreshToken").errMsg("Refresh Token 이 올바르지 않습니다").build();

        // STEP 2 : refresh token 이 만료되었는지 확인
        else if(!jwtTokenProvider.validateTokenExceptExpiration(token.getRefreshToken()))
            return SecurityDto.builder().errCode("wrongRefreshToken").errMsg("Refresh Token 이 만료되었습니다").build();

        else
            return null;

    }
}
