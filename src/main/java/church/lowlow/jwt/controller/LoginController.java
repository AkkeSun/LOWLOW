package church.lowlow.jwt.controller;

import church.lowlow.jwt.entity.UserDto;
import church.lowlow.jwt.resource.LoginErrorResource;
import church.lowlow.jwt.resource.UserResource;
import church.lowlow.jwt.service.UserService;
import church.lowlow.jwt.validation.UserValidation;
import church.lowlow.rest_api.common.aop.LogComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class LoginController {

    private final LogComponent logComponent;
    private final UserService service;
    private final UserValidation validation;

    @PostMapping
    public ResponseEntity login(@RequestBody UserDto dto, Errors errors) {

        logComponent.userDtoLogging(dto);

        // check
        validation.duplicated(dto, errors, "login");
        if(errors.hasErrors())
            return badRequest().body(new LoginErrorResource(errors));

        UserDto loginUser = service.userLogin(dto);
        UserResource resource = new UserResource(loginUser);

        return ResponseEntity.ok(resource);
    }

}
