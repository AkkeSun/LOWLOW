package church.lowlow;

import church.lowlow.jwt.entity.User;
import church.lowlow.jwt.entity.UserDto;
import church.lowlow.jwt.repository.UserRepository;
import church.lowlow.jwt.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
public class JwtRunner implements ApplicationRunner {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService service;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        setDev("akkessun@gmail.com", "1234");
        setAdmin("admin@gmail.com", "1234");
        setUploader("uploader@gmail.com", "1234");

    }


    private void setDev(String email, String password) {

        Optional<User> dev = repository.findByEmail(email);

        if(dev.isEmpty()) {
            UserDto user = UserDto.builder().email(email).password(password).role("ROLE_DEV").build();
            service.userRegister(user);
            log.info("~~~~~~~ New DEV Saved ~~~~~~~~");
        }
    }

    private void setAdmin(String email, String password) {

        Optional<User> admin = repository.findByEmail(email);

        if(admin.isEmpty()) {
            UserDto user = UserDto.builder().email(email).password(password).role("ROLE_ADMIN").build();
            service.userRegister(user);
            log.info("~~~~~~~ New ADMIN Saved  ~~~~~~~~");
        }
    }

    private void setUploader(String email, String password) {

        Optional<User> uploader = repository.findByEmail(email);

        if(uploader.isEmpty()) {
            UserDto user = UserDto.builder().email(email).password(password).role("ROLE_UPLOADER").build();
            service.userRegister(user);
            log.info("~~~~~~~ New UPLOADER Saved  ~~~~~~~~");
        }
    }
}
