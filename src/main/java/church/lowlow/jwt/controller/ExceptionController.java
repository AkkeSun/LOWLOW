package church.lowlow.jwt.controller;

import church.lowlow.jwt.entity.TokenDto;
import church.lowlow.jwt.resource.TokenErrorsResource;
import church.lowlow.jwt.resource.TokenResource;
import church.lowlow.jwt.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@RestController
@RequestMapping(value = "/exception", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class ExceptionController {

    @Autowired
    private UserService service;

    @GetMapping("/access")
    public ResponseEntity nonLogin(HttpServletRequest request)
    {

        String accessToken = request.getHeader("ACCESS-TOKEN");

        // 토큰이 존재한다면 (토큰이 만료 되었다면) -> ACCESS-TOKEN 신규 발급
        if (accessToken != null)
        {
            String refreshToken = request.getHeader("REFRESH-TOKEN");
            TokenDto token = TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();

            TokenDto newToken = service.issueAccessToken(token);
            Resource resource = new TokenResource(newToken);

            return ResponseEntity.ok(resource);
        }

        TokenDto token = TokenDto.builder().errCode("access").build();
        Resource resource = new TokenErrorsResource(token);
        return  ResponseEntity.badRequest().body(resource);
    }


    @GetMapping("/access-denied")
    public ResponseEntity denied(){

        log.info("[DENIED] 접근 권한 이 없습니다");

        TokenDto token = TokenDto.builder().errCode("access-denied").build();
        Resource resource = new TokenErrorsResource(token);
        return  ResponseEntity.badRequest().body(resource);
    }
}
