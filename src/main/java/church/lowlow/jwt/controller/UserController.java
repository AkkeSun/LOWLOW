package church.lowlow.jwt.controller;

import church.lowlow.jwt.entity.UserDto;
import church.lowlow.jwt.resource.UserErrorsResource;
import church.lowlow.jwt.resource.UserResource;
import church.lowlow.jwt.service.UserService;
import church.lowlow.jwt.validation.UserValidation;
import church.lowlow.rest_api.accounting.controller.AccountingController;
import church.lowlow.rest_api.common.aop.LogComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/jwt", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class UserController {

    private final UserService service;

    private final UserValidation validation;

    private final LogComponent logComponent;


    /*******************************************
     *                  CREATE API
     ********************************************/
    @PostMapping
    public ResponseEntity register(@RequestBody UserDto dto, Errors errors) {

        logComponent.userDtoLogging(dto);

        // check
        validation.duplicated(dto, errors, "register");
        if (errors.hasErrors())
            return badRequest().body(new UserErrorsResource(errors));

        // save
        UserDto newUser = service.userRegister(dto);
        URI createdUri = linkTo(UserController.class).slash(newUser.getId()).toUri();

        // return
        UserResource resource = new UserResource(newUser);
        resource.add(linkTo(AccountingController.class).slash(newUser.getId()).withRel("update-user"));
        resource.add(linkTo(AccountingController.class).slash(newUser.getId()).withRel("delete-user"));

        return ResponseEntity.created(createdUri).body(resource);
    }




    /*******************************************
     *                  READ API
     ********************************************/
    /*
    // ======================== paging data ========================
    @GetMapping
    public ResponseEntity getAccountingPage(SearchDto searchDto, PagingDto pagingDto,
                                            PagedResourcesAssembler<User> assembler) {

        // request Logging
        logComponent.pagingDtoLogging(pagingDto);

        // load
        Page<User> page = service.getUserPage(pagingDto);

        // return
        var pagedResources = assembler.toResource(page, e -> new UserResource(e));
        return ResponseEntity.ok(pagedResources);
    }
     */

    @GetMapping("/{id}")
    public ResponseEntity getMember(@PathVariable Integer id, Errors errors){

        // check
        validation.duplicated(UserDto.builder().id(id).build(), errors, "find");
        if (errors.hasErrors())
            return badRequest().body(new UserErrorsResource(errors));

        UserDto user = service.getUser(id);

        UserResource resource = new UserResource(user);
        resource.add(linkTo(UserController.class).slash(user.getId()).withRel("update-user"));
        resource.add(linkTo(UserController.class).slash(user.getId()).withRel("delete-user"));
        return ResponseEntity.ok(resource);
    }

    /**************************
     *       UPDATE API
     **************************/
    @PutMapping("/{id}")
    public ResponseEntity updateMembers(@RequestBody UserDto dto, @PathVariable Integer id, Errors errors) {

        // request param logging
        logComponent.userDtoLogging(dto);

        // check
        validation.duplicated(UserDto.builder().id(id).build(), errors, "find");
        if (errors.hasErrors())
            return badRequest().body(new UserErrorsResource(errors));

        UserDto updateUser = service.updateUser(id, dto);

        // return
        UserResource resource = new UserResource(updateUser);
        resource.add(linkTo(UserController.class).slash(updateUser.getId()).withRel("delete-user"));

        return ResponseEntity.ok(resource);
    }



    /**************************
     *       DELETE API
     **************************/
    @DeleteMapping("/{id}")
    public ResponseEntity deleteMembers(@PathVariable Integer id, Errors errors) {

        // check
        validation.duplicated(UserDto.builder().id(id).build(), errors, "find");
        if (errors.hasErrors())
            return badRequest().body(new UserErrorsResource(errors));

        UserDto updateUser = service.deleteUser(id);

        // return
        UserResource resource = new UserResource(updateUser);
        return ResponseEntity.ok(resource);
    }

}
