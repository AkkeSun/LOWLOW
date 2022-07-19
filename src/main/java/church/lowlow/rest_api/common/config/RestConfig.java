package church.lowlow.rest_api.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 빈 등록을 위한 클래스
 */
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class RestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    private final String version = "LOWLOW v1";

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("LOWLOW API")
                .description("LOWLOW API 문서")
                .version("v1")
                .build();
    }

    @Bean
    public Docket commonApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(version)
                .useDefaultResponseMessages(false) // 불필요한 응답코드와 설명 제거
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("church.lowlow.rest_api")) // api 탐색 위치
                .paths(PathSelectors.ant("/api/**")) // path 조건
                .build();
    }

}
