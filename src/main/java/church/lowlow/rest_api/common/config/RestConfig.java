package church.lowlow.rest_api.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 빈 등록을 위한 클래스
 */
@Configuration
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

    @Bean
    public WebClient webClient(){
        String BASE_URL = "http://localhost:8080/api";
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

}
