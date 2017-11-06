package transfer.service.ingenico.gateway.h2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import transfer.service.ingenico.domains.Account;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class AccountGatewayTest {

    @InjectMocks
    private AccountGatewayImpl accountGateway;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void saveAccount() {
        //Given a account
        Account account = Account.builder().name("Rafael").currentBalance(BigDecimal.TEN).build();

        //When I try save the transfer
        accountGateway.saveUpdate(account);

        //Then I verify if save method was invoked
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void find() {
        //given an account id
        Long accountId = 1l;

        //When I try find the transfer
        accountGateway.find(accountId);

        //Then I verify if find method was invoked
        verify(accountRepository, times(1)).findOne(accountId);
    }
}
