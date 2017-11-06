package transfer.service.ingenico.integrationTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.gateway.AccountRepository;
import transfer.service.ingenico.gateway.http.json.AccountRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateAccountIntegrationTest {

    private static final String URL = "/api/v1/account";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void clean() {
        accountRepository.deleteAll();
    }

    @Test
    public void createClient() {

        AccountRequest accountRequest = AccountRequest.builder().name("Rafael").currentBalance(BigDecimal.TEN).build();
        ResponseEntity responseEntity = restTemplate.postForEntity(URL, accountRequest, void.class);
        List<Account> accounts = accountRepository.findAll();
        assertEquals(accounts.size(), 1);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }
}
