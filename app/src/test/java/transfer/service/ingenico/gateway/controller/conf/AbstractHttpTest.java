package transfer.service.ingenico.gateway.controller.conf;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import transfer.service.ingenico.domains.Exceptions.CustomExceptionHandler;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public abstract class AbstractHttpTest {

    protected MockMvc buildMockMvcWithBusinessExecptionHandler(final Object controller) {
        return standaloneSetup(controller).setControllerAdvice(new CustomExceptionHandler()).build();
    }
}
