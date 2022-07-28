package church.lowlow.jwt.service;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("아이디 혹은 비밀번호가 올바르지 않습니다"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new RuntimeException("아이디 혹은 비밀번호가 올바르지 않습니다");

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

        log.info("[issueAccessToken]");

        // STEP 1 : refresh token 이 만료되었는지 확인
        if(!jwtTokenProvider.validateTokenExceptExpiration(tokenDto.getRefreshToken()))
            throw new RuntimeException("Refresh Token 이 만료되었습니다");

        // STEP 2 : 유저가 보낸 refresh 토큰과 db에 저장된 refresh 토큰이 같은지 확인
        User user = repository.findByRefreshToken(tokenDto.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh Token 이 유효하지 않습니다"));

        log.info("[Refresh Token Check Success]");

        // STEP 3 : Access Token 신규 발급
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
    public UserDto getUser (Integer id){
        User user = repository.findById(id).orElseThrow(ArithmeticException::new);
        return userDtoBuilder(user);
    }

    @Transactional
    public Object updateUser (Integer id, UserDto dto, Errors errors) {

        Optional<User> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = modelMapper.map(dto, User.class);
        user.setId(id);
        User updateUser = repository.save(user);
        return userDtoBuilder(updateUser);
    }


    @Transactional
    public Object deleteUser(Integer id) {

        Optional<User> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

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
