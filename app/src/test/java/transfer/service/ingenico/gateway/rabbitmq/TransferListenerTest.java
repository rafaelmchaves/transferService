package transfer.service.ingenico.gateway.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import transfer.service.ingenico.domains.Exceptions.InsufficientBalanceException;
import transfer.service.ingenico.domains.Exceptions.NotFoundAccountException;
import transfer.service.ingenico.usecases.TransferMoney;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TransferListenerTest {

    @InjectMocks
    private TransferListener transferListener;

    @Mock
    private TransferMoney transferMoney;

    @Test
    public void transferWithSuccess() throws NotFoundAccountException, InsufficientBalanceException {
        //given transferId equals 1
        Long transferId = 1L;

        //when I try make a transfer
        transferListener.transfer(transferId);

        //then transfer money method was invoked
        verify(transferMoney, times(1)).execute(transferId);
    }
}
