package church.lowlow.jwt.controller;

import church.lowlow.jwt.entity.TokenDto;
import church.lowlow.jwt.resource.TokenResource;
import church.lowlow.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/jwt", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class TokenController {

    private final UserService service;

    @PostMapping("/issueAccessToken")
    public ResponseEntity issueAccessToken(@RequestBody TokenDto tokenDto) {
        TokenDto token = service.issueAccessToken(tokenDto);
        Resource resource = new TokenResource(token);
        return ResponseEntity.ok(resource);
    }

}