package church.lowlow.jwt.service;

import church.lowlow.jwt.controller.UserController;
import church.lowlow.jwt.entity.ErrorDto;
import church.lowlow.jwt.entity.TokenDto;
import church.lowlow.jwt.entity.User;
import church.lowlow.jwt.entity.UserDto;
import church.lowlow.jwt.repository.UserRepository;
import church.lowlow.jwt.security.JwtTokenProvider;
import church.lowlow.rest_api.common.entity.PagingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.badRequest;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;

    @Transactional
    public UserDto userRegister(UserDto dto) {

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(roleSetting(dto.getRole()))
                .build();
        User newUser = repository.save(user);

        return userDtoBuilder(newUser);
    }


    @Transactional
    public UserDto userLogin(UserDto dto){

        User user = repository.findByEmail(dto.getEmail()).get();

        // refresh token DB 저장
        user.setRefreshToken(jwtTokenProvider.createRefreshToken());
        User loginUser = repository.save(user);

        // access token 을 발급받은 후 password 에 저장 후 리턴
        UserDto userDto = userDtoBuilder(loginUser);
        userDto.setPassword(jwtTokenProvider.createToken(loginUser.getEmail()));

        log.info("LOGIN SUCCESS");
        log.info("Access-token : " + userDto.getPassword());
        log.info("Refresh-token : " + userDto.getRefreshToken());

        return userDto;
    }


    @Transactional
    public TokenDto issueAccessToken (TokenDto tokenDto){

        User user = repository.findByRefreshToken(tokenDto.getRefreshToken()).get();

        // Token 신규 발급
        String newAccessToken = jwtTokenProvider.createToken(user.getEmail());
        String newRefreshToken = jwtTokenProvider.createRefreshToken();

        user.setRefreshToken(newRefreshToken);
        repository.save(user);
        log.info("[New Token Created]");
        log.info("[New Access Token] " + newAccessToken);
        log.info("[New Refresh Token] " + newRefreshToken);

        return TokenDto.builder().refreshToken(newRefreshToken).accessToken(newAccessToken).build();
    }


    @Transactional
    public Page<User> getUserPage (PagingDto pagingDto){
        Pageable pageable = PageRequest.of(pagingDto.getNowPage(), 10);
        return repository.findAll(pageable);
    }

    @Transactional
    public Object getUser (Integer id){

        Optional<User> optional = repository.findById(id);
        if(optional.isEmpty())
        {
            Link link = linkTo(UserController.class).slash(id).withSelfRel();
            ErrorDto dto = ErrorDto.builder().errCode("getUserErr").errMsg("유저를 찾을 수 없습니다").build();
            return badRequest().body(new Resource(dto, link));
        }

        return userDtoBuilder(optional.get());
    }

    @Transactional
    public Object updateUser (Integer id, UserDto userDto) {

        Optional<User> optional = repository.findById(id);
        if(optional.isEmpty())
        {
            Link link = linkTo(UserController.class).slash(id).withSelfRel();
            ErrorDto dto = ErrorDto.builder().errCode("getUserErr").errMsg("유저를 찾을 수 없습니다").build();
            return badRequest().body(new Resource(dto, link));
        }

        User user = modelMapper.map(userDto, User.class);
        user.setId(id);
        User updateUser = repository.save(user);
        return userDtoBuilder(updateUser);

    }


    @Transactional
    public Object deleteUser(Integer id) {

        Optional<User> optional = repository.findById(id);
        if(optional.isEmpty())
        {
            Link link = linkTo(UserController.class).slash(id).withSelfRel();
            ErrorDto dto = ErrorDto.builder().errCode("getUserErr").errMsg("유저를 찾을 수 없습니다").build();
            return badRequest().body(new Resource(dto, link));
        }

        User removeUser = optional.get();
        repository.delete(removeUser);

        return userDtoBuilder(removeUser);
    }



    private UserDto userDtoBuilder(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(jwtTokenProvider.createToken(user.getEmail()))
                .refreshToken(user.getRefreshToken())
                .role(user.getRoles().get(0))
                .build();
    }


    private List <String> roleSetting(String role) {

        List <String> roles = new ArrayList<>();
        if(role.equals("ROLE_UPLOADER"))
        {
            roles.add("ROLE_UPLOADER");
        }
        else if(role.equals("ROLE_ADMIN"))
        {
            roles.add("ROLE_ADMIN");
            roles.add("ROLE_UPLOADER");
        }
        else
        {
            roles.add("ROLE_DEV");
            roles.add("ROLE_ADMIN");
            roles.add("ROLE_UPLOADER");
        }
        return roles;
    }
}
