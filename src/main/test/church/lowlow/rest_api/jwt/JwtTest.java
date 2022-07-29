package church.lowlow.rest_api.jwt;

import church.lowlow.jwt.entity.UserDto;
import church.lowlow.jwt.service.UserService;
import church.lowlow.rest_api.common.BaseControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class JwtTest extends BaseControllerTest {


    @Autowired
    private UserService service;

    @Test
    @Description("테스트를 위한 개발자 계정 신규등록")
    public void createDevRoles() throws Exception {

        // given
        UserDto dto = UserDto.builder()
                .email("akkessun@gmail.com")
                .password("1234")
                .role("ROLE_DEV")
                .build();

        // when
        service.userRegister(dto);

    }


    @Test
    @Description("로그인 테스트")
    public void createMember() throws Exception {

        // given
        String url = "/login";
        UserDto user = UserDto.builder()
                .email("akkessun@gmail.com")
                .password("1234")
                .build();

        // when
        ResultActions actions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON_UTF8_VALUE)
                .content(mapper.writeValueAsString(user)));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,  MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("password").exists())
                .andExpect(jsonPath("roles").exists())
                .andDo(print());
    }
}
