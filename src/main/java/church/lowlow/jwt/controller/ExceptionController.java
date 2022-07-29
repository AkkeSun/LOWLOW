package church.lowlow.jwt.controller;

import church.lowlow.jwt.entity.ErrorDto;
import church.lowlow.jwt.entity.TokenDto;
import church.lowlow.jwt.service.UserService;
import church.lowlow.jwt.validation.TokenValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.badRequest;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/exception", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class ExceptionController {

    private final UserService service;
    private final TokenValidation tokenValidation;

    @GetMapping("/authentication")
    public ResponseEntity nonLogin(@RequestHeader(value="ACCESS-TOKEN",  required = false) String accessToken,
                                   @RequestHeader(value="REFRESH-TOKEN", required = false) String refreshToken) {


        Link link = linkTo(ExceptionController.class).slash("authentication").withSelfRel();

        // ~~~~~~~~~ 토큰이 존재한다면 (토큰이 만료 되었다면) -> TOKEN 신규 발급 ~~~~~~~~~
        if (accessToken != null && refreshToken != null)
        {

            TokenDto token = TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();

            // 토큰 유효성 검증
            ErrorDto validationResult = tokenValidation.duplicated(token);
            if(validationResult != null) {
                return badRequest().body(new Resource(validationResult, link));
            }

            // 토큰 신규발급
            TokenDto newToken = service.issueAccessToken(token);

            return ResponseEntity.ok(new Resource(newToken, link));
        }

        // ~~~~~~~~~ 토큰이 존재하지 않는다면 (인증하지 않은 상태) ~~~~~~~~~
        log.info("[AUTHENTICATION] 인증이 필요한 서비스입니다");
        ErrorDto errorDto = ErrorDto.builder().errCode("Authentication Error").errMsg("인증이 필요한 서비스입니다").build();

        return  badRequest().body(new Resource(errorDto, link));
    }


    @GetMapping("/access-denied")
    public ResponseEntity denied(){

        Link link = linkTo(ExceptionController.class).slash("/access-denied").withSelfRel();

        log.info("[ACCESS DENIED] 접근 권한이 없습니다");
        ErrorDto securityDto = ErrorDto.builder().errCode("access-denied").errMsg("접근 권한이 없습니다").build();

        return  badRequest().body(new Resource(securityDto, link));
    }
}
