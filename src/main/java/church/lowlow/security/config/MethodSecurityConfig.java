package church.lowlow.security.config;

import church.lowlow.security.factory.MethodResourcesMapFactoryBean;

import church.lowlow.security.service.SecurityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    private SecurityResourceService securityResourceService;


    // =========================== factory bean ===============================
    @Bean
    public MethodResourcesMapFactoryBean methodResourcesMapFactoryBean(){
        return new MethodResourcesMapFactoryBean(securityResourceService);
    }

    // =========================== MetaData Source ===============================
    @Bean
    public MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource(){
        return new MapBasedMethodSecurityMetadataSource(methodResourcesMapFactoryBean().getObject());
    }

    // =========================== Method 기반 인가 적용 ===============================
    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return mapBasedMethodSecurityMetadataSource();
    }

}
