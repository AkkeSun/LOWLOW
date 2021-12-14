package church.lowlow.security;

import church.lowlow.security.factory.MethodResourcesMapFactoryBean;
import church.lowlow.security.factory.UrlResourcesMapFactoryBean;
import church.lowlow.security.handler.CustomAccessDeniedHandler;
import church.lowlow.security.handler.CustomAuthenticationFailureHandler;
import church.lowlow.security.handler.CustomAuthenticationSuccessHandler;
import church.lowlow.security.service.SecurityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
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
@EnableWebSecurity
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
    //============ Login처리 ============
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
                .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class)
                .exceptionHandling()
                .accessDeniedHandler(deniedHandler)

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
        return  new RoleHierarchyImpl();
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

    @Bean
    public MethodResourcesMapFactoryBean methodResourcesMapFactoryBean(){
        return new MethodResourcesMapFactoryBean(securityResourceService);
    }

    // =========================== MetaData Source ===============================
    @Bean
    public UrlMetadataSource urlMetadataSource() {
        return new UrlMetadataSource(urlResourcesMapFactoryBean().getObject());
    }

    @Bean // 스프링 기본제공
    public MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource(){
        return new MapBasedMethodSecurityMetadataSource(methodResourcesMapFactoryBean().getObject());
    }

    // ================== DB 인가처리 Filter ================
    private String[] permitAllResources = {"/", "/adminLogin", "/adminLogin*", "/adminLogout"};
    @Bean
    public PermitAllFilter customFilterSecurityInterceptor() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllResources);
        permitAllFilter.setSecurityMetadataSource(urlMetadataSource());      // url 시큐리티 인가 정보
        permitAllFilter.setAccessDecisionManager(accessDecisionManager());   // 접근 결정 매니저
        permitAllFilter.setAuthenticationManager(authenticationManager());   // 인증 매니저
        return permitAllFilter;
    }
}
