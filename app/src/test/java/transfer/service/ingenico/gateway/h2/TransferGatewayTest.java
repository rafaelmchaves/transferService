package transfer.service.ingenico.gateway.h2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.domains.Transfer;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class TransferGatewayTest {

    @InjectMocks
    private TransferGatewayImpl transferGateway;

    @Mock
    private TransferRepository transferRepository;

    @Test
    public void saveTransfer() {

        //Given a sender account
        Account senderAccount = Account.builder().name("Rafael").currentBalance(BigDecimal.TEN).build();
        //and a recipient account
        Account recipientAccount = Account.builder().name("Amanda").currentBalance(BigDecimal.ONE).build();
        //and a transfer between this two accounts
        Transfer transfer = Transfer.builder().
                senderAccountId(senderAccount).
                recipientAccountId(recipientAccount).
                value(BigDecimal.ONE).build();

        //When I try save the transfer
        transferGateway.save(transfer);

        //Then I verify if save method was invoked
        verify(transferRepository, times(1)).save(transfer);
    }
}
