package church.lowlow.rest_api.common;

import church.lowlow.jwt.entity.UserDto;
import church.lowlow.jwt.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest // 통합테스트
@AutoConfigureMockMvc // 통합테스트에서 MockMvc 를 사용하기 위한 설정
@AutoConfigureRestDocs // rest docs 사용
@Import(RestDocsConfiguration.class) // 커스텀한 RestDocsConfiguration bean 사용
@ActiveProfiles("test") // 테스트용 profile 사용
@Ignore // 테스트를 실행하는 클래스로 간주되지 않도록 설정
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected UserService userService;


    @Description("ROLE_DEV 로그인")
    protected UserDto devLogin() {
        UserDto dev = UserDto.builder()
                .email("akkessun@gmail.com")
                .password("1234")
                .build();

        return userService.userLogin(dev);
    }

    @Description("ROLE_ADMIN 로그인")
    protected UserDto adminLogin() {
        UserDto admin = UserDto.builder()
                .email("admin@gmail.com")
                .password("1234")
                .build();

        return userService.userLogin(admin);
    }

    @Description("ROLE_UPLOADER 로그인")
    protected UserDto uploaderLogin() {
        UserDto uploader = UserDto.builder()
                .email("uploader@gmail.com")
                .password("1234")
                .build();

        return userService.userLogin(uploader);
    }

}
