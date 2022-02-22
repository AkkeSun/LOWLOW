package church.lowlow.security.config;

import church.lowlow.security.metadata.UrlMetadataSource;
import church.lowlow.security.LoginFilter;
import church.lowlow.security.LoginProvider;
import church.lowlow.security.PermitAllFilter;
import church.lowlow.security.factory.UrlResourcesMapFactoryBean;
import church.lowlow.security.handler.CustomAccessDeniedHandler;
import church.lowlow.security.handler.CustomAuthenticationFailureHandler;
import church.lowlow.security.handler.CustomAuthenticationSuccessHandler;
import church.lowlow.security.service.SecurityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity // spring security 사용
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginProvider ajaxLoginProvider;
    @Autowired
    private CustomAccessDeniedHandler deniedHandler;
    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;
    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;
    @Autowired
    private SecurityResourceService securityResourceService;


    @Override
    //============ Login 처리 ============
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxLoginProvider);
    }

    @Override
    //============ 정적파일 처리 ============
    public void configure(WebSecurity web)  {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/favicon.ico", "/resources/**", "/error");
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        //============ 인가 처리 ============
        http
                // 기본적으로 사용하는 FilterSecurityInterceptor 보다 커스텀 필터를 앞에 두어야 한다
                .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class)
                .exceptionHandling()
                .accessDeniedHandler(deniedHandler) // 접근 권한이 없는 사람이 접근 시 실행되는 핸들러
        ;

        //=========== 인증 처리 ===============
        http
                .formLogin()
                .loginPage("/adminLogin")
        .and()
                .addFilterBefore(ajaxLoginFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    //============================ 인증매니저 =============================
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    // ======================= Ajax Login Filter =========================
    public LoginFilter ajaxLoginFilter () throws Exception {
        LoginFilter filter = new LoginFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }


    // ========================== Role 계층권한 적용 ===========================
    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        return new RoleHierarchyImpl();
    }
    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    // =============================접근결정 매니저 ==============================
    @Bean
    public AccessDecisionManager accessDecisionManager(){
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(roleVoter());  // Role 계층권한 적용
        return new AffirmativeBased(accessDecisionVoters);
    }


    // =========================== factory bean ===============================
    @Bean
    public UrlResourcesMapFactoryBean urlResourcesMapFactoryBean(){
        return new UrlResourcesMapFactoryBean(securityResourceService);
    }

    // =========================== MetaData Source ===============================
    @Bean
    public UrlMetadataSource urlMetadataSource() {
        return new UrlMetadataSource(urlResourcesMapFactoryBean().getObject());
    }


    // ================== DB 인가처리 Filter ================
    @Value("${permitAllResources}")
    private String[] permitAllResources;
    @Bean
    public PermitAllFilter customFilterSecurityInterceptor() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllResources); // 누구나 접근 가능한 Path 설정 (시큐리티 필터링 전에 처리함)
        permitAllFilter.setSecurityMetadataSource(urlMetadataSource());            // url 시큐리티 인가 정보
        permitAllFilter.setAccessDecisionManager(accessDecisionManager());         // 접근 결정 매니저
        permitAllFilter.setAuthenticationManager(authenticationManager());         // 인증 매니저
        return permitAllFilter;
    }
}
