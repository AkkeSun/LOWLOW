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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity // spring security ??????
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

    @Value("${security.csrf.permitALlResources}")
    private String[] csrfPermitAllResource;

    @Value("${security.permitAllResources}")
    private String[] permitAllResources;

    
    @Override
    //============ Login ?????? ============
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxLoginProvider);
    }

    @Override
    //============ ???????????? ?????? ============
    public void configure(WebSecurity web)  {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/favicon.ico", "/resources/**", "/error");
    }



    @Override
    public void configure(HttpSecurity http) throws Exception {

        //============ ?????? ?????? ============
        http
                .csrf().ignoringAntMatchers(csrfPermitAllResource) // csrf ????????????
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class)
                .exceptionHandling()
                .accessDeniedHandler(deniedHandler) // ?????? ????????? ?????? ????????? ?????? ??? ???????????? ?????????
        ;

        //=========== ?????? ?????? ===============
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
    //============================ ??????????????? =============================
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


    // ========================== Role ???????????? ?????? ===========================
    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        return new RoleHierarchyImpl();
    }
    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    // =============================???????????? ????????? ==============================
    @Bean
    public AccessDecisionManager accessDecisionManager(){
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(roleVoter());  // Role ???????????? ??????
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


    // ================== DB ???????????? Filter ================
    @Bean
    public PermitAllFilter customFilterSecurityInterceptor() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllResources); // ????????? ?????? ????????? get Path ?????? (???????????? ????????? ?????? ?????????)
        permitAllFilter.setSecurityMetadataSource(urlMetadataSource());            // url ???????????? ?????? ??????
        permitAllFilter.setAccessDecisionManager(accessDecisionManager());         // ?????? ?????? ?????????
        permitAllFilter.setAuthenticationManager(authenticationManager());         // ?????? ?????????
        return permitAllFilter;
    }
}
