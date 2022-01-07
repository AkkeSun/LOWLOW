package church.lowlow.user_api.common;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ServerStartCheck implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.err.println("==============================================================================================================================================================================");
        System.err.println("=============================================================          LOWLOW SERVER START SUCCESS       =====================================================================");
        System.err.println("==============================================================================================================================================================================");
        System.err.println();System.err.println(); System.err.println();System.err.println(); System.err.println();System.err.println();System.err.println();System.err.println();System.err.println();System.err.println();System.err.println();
    }
}
