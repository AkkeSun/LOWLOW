package church.lowlow.jwt.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    // 권한 심사 받지 않는 파일들
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()) // 기본 설정된 모든 정적파일들
                .antMatchers("/favicon.ico", "/resources/**", "/exception/**", "/login");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 인증 정책
        http
                .httpBasic().disable() // rest api 이므로 기본설정 미사용
                .csrf().disable()      // rest api 이므로 csrf 보안 미사용
                .formLogin().disable() // form login 비활성화
                .cors().configurationSource(corsConfigurationSource()) // 시큐리티 cors 설정
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt 로 인증하므로 세션 미사용
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) // jwt 필터 추가
        ;


        // 인가정책 (순서 : permitAll() - dev - dev/admin - authenticated())
        http
                .authorizeRequests()

                // 홈페이지에 노출되는 정보들 (누구나 접근 가능)
                .mvcMatchers(HttpMethod.GET, "/api/calendars/**").permitAll() // api/calendars 하위의 모든 get method 허용
                .mvcMatchers(HttpMethod.GET, "/api/galleries/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/notices/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/weekly/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/worshipVideos/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/basicInfo/**").permitAll()

                // DEV 만 접근 가능 : jwt
                .antMatchers("/jwt/**").hasRole("DEV") // DB 입력은 [ROLE_000] 으로 하고 체크는 [ROLE_] 을 뺀다

                // ADMIN 까지 접근 가능 : accounting, basicInfo, members, memberAttend
                .mvcMatchers(HttpMethod.POST, "/api/accounting/**").hasAnyRole("ADMIN", "DEV")
                .mvcMatchers(HttpMethod.POST, "/api/basicInfo/**").hasAnyRole("ADMIN", "DEV")
                .mvcMatchers(HttpMethod.POST, "/api/members/**").hasAnyRole("ADMIN", "DEV")
                .mvcMatchers(HttpMethod.POST, "/api/memberAttend/**").hasAnyRole("ADMIN", "DEV")
                .mvcMatchers(HttpMethod.PUT, "/api/accounting/**").hasAnyRole("ADMIN", "DEV")
                .mvcMatchers(HttpMethod.PUT, "/api/basicInfo/**").hasAnyRole("ADMIN", "DEV")
                .mvcMatchers(HttpMethod.PUT, "/api/members/**").hasAnyRole("ADMIN", "DEV")
                .mvcMatchers(HttpMethod.PUT, "/api/memberAttend/**").hasAnyRole("ADMIN", "DEV")
                .mvcMatchers(HttpMethod.DELETE, "/api/accounting/**").hasAnyRole("ADMIN", "DEV")
                .mvcMatchers(HttpMethod.DELETE, "/api/basicInfo/**").hasAnyRole("ADMIN", "DEV")
                .mvcMatchers(HttpMethod.DELETE, "/api/members/**").hasAnyRole("ADMIN", "DEV")
                .mvcMatchers(HttpMethod.DELETE, "/api/memberAttend/**").hasAnyRole("ADMIN", "DEV")


                // 나머지는 인증했다면 누구나 접근 가능
                .anyRequest().authenticated()
        ;



        // 인증 예외 처리 (로그인 실패)
        http
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        response.sendRedirect("/exception/access");
                    }
                })
        ;



        // 인가 예외처리 (권한없는 URL 접속)
        http
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletResponse.sendRedirect("/exception/access-denied");
                    }
                })
        ;


    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Value("${cors.url}")
    private String webUrl;

    @Bean
    // spring security 를 사용하는 경우 cors 허용은 시큐리티에서 셋팅한다
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin(webUrl);        // 허용할 url
        configuration.addAllowedHeader("*");           // 허용할 header
        configuration.addAllowedMethod("*");           // 허용할 Http Method
        configuration.setAllowCredentials(true);       // 웹서버 쿠키 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
