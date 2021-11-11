package church.lowlow.rest_api.common.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 빈 등록을 위한 클래스
 */
@Configuration
public class RestConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
