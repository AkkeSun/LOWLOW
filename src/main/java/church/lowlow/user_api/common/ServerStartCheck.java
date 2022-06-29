package church.lowlow.user_api.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Log4j2
public class ServerStartCheck implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("");
        log.info("=====================================");
        log.info("==   LOWLOW SERVER START SUCCESS   ==");
        log.info("=====================================");
        log.info(""); log.info("");  log.info("");
    }
}
