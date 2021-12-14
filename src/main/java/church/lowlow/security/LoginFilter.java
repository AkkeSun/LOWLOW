package church.lowlow.security;

import church.lowlow.security.domain.dto.AccountDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ajax 로그인 요청처리 filter
 * UsernamePasswordAuthenticationFilter 대신 사용
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public LoginFilter(){
        super(new AntPathRequestMatcher("/ajax/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        // ajax 요청인지 확인
        if(!isAjax(request))
            throw new IllegalStateException("Authentication is not supported");

        // 유효성 검사
        AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);
        if(StringUtils.isEmpty(accountDto.getUsername()) || StringUtils.isEmpty(accountDto.getPassword()))
            throw new IllegalArgumentException("Username or Password is empty");

        // username, password 정보를 담은 token 생성
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());

        // details 설정
        setDetails(request, token);

        return getAuthenticationManager().authenticate(token);
    }


    private boolean isAjax(HttpServletRequest request) {
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-with")))
            return true;
        return false;
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
