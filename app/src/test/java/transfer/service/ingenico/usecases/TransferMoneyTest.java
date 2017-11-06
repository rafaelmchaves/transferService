package transfer.service.ingenico.usecases;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.domains.Exceptions.InsufficientBalanceException;
import transfer.service.ingenico.domains.Exceptions.NotFoundAccountException;
import transfer.service.ingenico.domains.Transfer;
import transfer.service.ingenico.gateway.AccountGateway;
import transfer.service.ingenico.gateway.TransferGateway;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransferMoneyTest {

    @InjectMocks
    private TransferMoney transferMoney;

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private TransferGateway transferGateway;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void transferMoneyWithSuccess() throws NotFoundAccountException, InsufficientBalanceException, InterruptedException {
        //given a transfer id
        Long transferId = 1L;

        //and exists a transfer that has a transferId equals 1 and transfer value equals 1
        Account senderAccount = Account.builder().name("Rafael").currentBalance(BigDecimal.TEN).id(1l).build();
        when(accountGateway.find(1l)).thenReturn(senderAccount);
        Account recipientAccount = Account.builder().name("Amanda").currentBalance(BigDecimal.ONE).id(2l).build();
        when(accountGateway.find(2l)).thenReturn(recipientAccount);
        Transfer transfer = Transfer.builder().id(1L).recipientAccountId(recipientAccount).senderAccountId(senderAccount).value(BigDecimal.ONE).build();
        when(transferGateway.find(transferId)).thenReturn(transfer);

        //When I try do the transfer
        transferMoney.execute(transferId);

        //Then I verify if sender Account has 9 euros as current balance
        ArgumentCaptor<Transfer> argument = ArgumentCaptor.forClass(Transfer.class);
        verify(transferGateway).save(argument.capture());
        assertEquals(argument.getValue().getSenderAccountId().getCurrentBalance(), BigDecimal.valueOf(9l));
        //and recipient account has 2 euros as current balance
        assertEquals(argument.getValue().getRecipientAccountId().getCurrentBalance(), BigDecimal.valueOf(2l));
        //and save methods was called
        verify(accountGateway).saveUpdate(argument.getValue().getSenderAccountId());
        verify(accountGateway).saveUpdate(argument.getValue().getRecipientAccountId());
    }

    @Test
    public void transferMoneyNonexistentSenderAccount() throws NotFoundAccountException, InsufficientBalanceException, InterruptedException {
        exception.expect(NotFoundAccountException.class);
        exception.expectMessage("Sender account not found");

        //given a transfer id
        Long transferId = 1L;

        //and exists a transfer that has a transferId equals 1 and transfer value equals 1, but sender account doesn't exist
        Account senderAccount = Account.builder().name("Rafael").currentBalance(BigDecimal.TEN).id(1l).build();;
        when(accountGateway.find(1l)).thenReturn(null);
        Account recipientAccount = Account.builder().name("Amanda").currentBalance(BigDecimal.ONE).id(2l).build();
        when(accountGateway.find(2l)).thenReturn(recipientAccount);
        Transfer transfer = Transfer.builder().id(1L).recipientAccountId(recipientAccount).senderAccountId(senderAccount).value(BigDecimal.ONE).build();
        when(transferGateway.find(transferId)).thenReturn(transfer);

        //When I try do the transfer
        transferMoney.execute(transferId);

        // Then should raise a NotFoundAccountException with the following message: Sender account not found

    }

    @Test
    public void transferMoneyNonexistentRecipientAccount() throws NotFoundAccountException, InsufficientBalanceException, InterruptedException {
        exception.expect(NotFoundAccountException.class);
        exception.expectMessage("Recipient account not found");

        //given a transfer id
        Long transferId = 1L;

        //and exists a transfer that has a transferId equals 1 and transfer value equals 1 but recipient account doesn't exist
        Account senderAccount = Account.builder().name("Rafael").currentBalance(BigDecimal.TEN).id(1l).build();
        when(accountGateway.find(1l)).thenReturn(senderAccount);
        Account recipientAccount = Account.builder().name("Amanda").currentBalance(BigDecimal.ONE).id(2l).build();
        when(accountGateway.find(2l)).thenReturn(null);
        Transfer transfer = Transfer.builder().id(1L).recipientAccountId(recipientAccount).senderAccountId(senderAccount).value(BigDecimal.ONE).build();
        when(transferGateway.find(transferId)).thenReturn(transfer);

        //When I try do the transfer
        transferMoney.execute(transferId);

        // Then should raise a NotFoundAccountException with the following message: Recipient account not found
    }

    @Test
    public void transferMoneyInsufficientBalance() throws NotFoundAccountException, InsufficientBalanceException, InterruptedException {
        exception.expect(InsufficientBalanceException.class);
        exception.expectMessage("Sender have an insufficient balance to transfer");

        //given a transfer id
        Long transferId = 1L;

        //and exists a transfer that has a transferId equals 1 and transfer value equals 1
        Account senderAccount = Account.builder().name("Rafael").currentBalance(BigDecimal.TEN).id(1l).build();
        when(accountGateway.find(1l)).thenReturn(senderAccount);
        Account recipientAccount = Account.builder().name("Amanda").currentBalance(BigDecimal.ONE).id(2l).build();
        when(accountGateway.find(2l)).thenReturn(recipientAccount);
        //and transfer value is equal 11 euros
        Transfer transfer = Transfer.builder().id(1L).recipientAccountId(recipientAccount).senderAccountId(senderAccount).value(BigDecimal.valueOf(11l)).build();
        when(transferGateway.find(transferId)).thenReturn(transfer);

        //When I try do the transfer
        transferMoney.execute(transferId);

        // Then should raise a InsufficientBalanceException with the following message: Insufficient Balance
    }
}
