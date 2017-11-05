package transfer.service.ingenico.usecases;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.gateway.AccountGateway;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class CreateAccountTest {

    @InjectMocks
    private CreateAccount createAccount;

    @Mock
    private AccountGateway accountGateway;

    @Test
    public void testSuccess() {
        //given a account
        Account account = Account.builder().name("Rafael").currentBalance(BigDecimal.TEN).build();
        //when I try save the account
        createAccount.execute(account);
        //then I verify if save method was invoked
        Mockito.verify(accountGateway).saveUpdate(account);
    }
}
