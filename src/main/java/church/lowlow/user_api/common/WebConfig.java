package church.lowlow.user_api.common;

import church.lowlow.user_api.admin.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer{


    @Value("${fileUploadPath}")
    private String fileUploadPath;

    @Value("${restApiBaseUrl}")
    private String restApiBaseUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + fileUploadPath)
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> URL_PATTERNS = Arrays.asList("/admin/security/*", "/admin/security/*/*");

        registry.addInterceptor(new SecurityInterceptor())
                .addPathPatterns(URL_PATTERNS)
                .order(0);
    }


    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(restApiBaseUrl)
                .build();
    }

}
