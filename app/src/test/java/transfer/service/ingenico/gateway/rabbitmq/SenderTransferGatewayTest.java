package transfer.service.ingenico.gateway.rabbitmq;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.domains.Exceptions.InsufficientBalanceException;
import transfer.service.ingenico.domains.Exceptions.NotFoundAccountException;
import transfer.service.ingenico.domains.Transfer;
import transfer.service.ingenico.domains.TransferStatus;
import transfer.service.ingenico.gateway.AccountGateway;
import transfer.service.ingenico.gateway.SenderTransferGateway;
import transfer.service.ingenico.gateway.TransferGateway;
import transfer.service.ingenico.usecases.TransferMoney;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SenderTransferGatewayTest {

    @InjectMocks
    private SenderTransferGatewayImpl senderTransferGateway;

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private TransferGateway transferGateway;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void sendWithSuccess() throws NotFoundAccountException, InsufficientBalanceException {
        //Long senderId, Long recipientId, BigDecimal transferValue
        //given a senderId equals 1
        Long sendId = 1L;
        //and a recipientId equals 2
        Long recipient = 2L;
        //and transferValue equals 10
        BigDecimal transferValue = BigDecimal.TEN;

        //and senderId matches with existent sender account
        Account senderAccount = Account.builder().name("Rafael").currentBalance(BigDecimal.TEN).build();
        when(accountGateway.find(sendId)).thenReturn(senderAccount);
        //and recipientId matches with existent recipient account
        Account recipientAccount = Account.builder().name("Amanda").currentBalance(BigDecimal.TEN).build();
        when(accountGateway.find(recipient)).thenReturn(recipientAccount);

        Transfer transfer = Transfer.builder().id(1L).value(BigDecimal.TEN).
                senderAccountId(senderAccount).
                recipientAccountId(recipientAccount).
                status(TransferStatus.RECEIVED).
                build();
        when(transferGateway.save(any())).thenReturn(transfer);

        //when I try to send this message
        senderTransferGateway.send(sendId, recipient, transferValue);
        //then send method is invoked
        ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), argument.capture());
        //and transferId was send
        assertEquals(argument.getValue(), transfer.getId());
    }

    @Test
    public void sendWithoutSenderId() throws NotFoundAccountException, InsufficientBalanceException {
        exception.expect(NotFoundAccountException.class);
        exception.expectMessage("Sender account not found");

        //Long senderId, Long recipientId, BigDecimal transferValue
        //given a senderId equals 1
        Long sendId = 1L;
        //and a recipientId equals 2
        Long recipient = 2L;
        //and transferValue equals 10
        BigDecimal transferValue = BigDecimal.TEN;

        //and senderId matches with existent sender account
        Account senderAccount = null;
        when(accountGateway.find(sendId)).thenReturn(senderAccount);
        //and recipientId matches with existent recipient account
        Account recipientAccount = Account.builder().name("Amanda").currentBalance(BigDecimal.TEN).build();
        when(accountGateway.find(recipient)).thenReturn(recipientAccount);

        Transfer transfer = Transfer.builder().id(1L).value(BigDecimal.TEN).
                senderAccountId(senderAccount).
                recipientAccountId(recipientAccount).
                status(TransferStatus.RECEIVED).
                build();
        when(transferGateway.save(any())).thenReturn(transfer);

        //when I try to send this message
        senderTransferGateway.send(sendId, recipient, transferValue);
        //then an exception was invoked

    }

    @Test
    public void sendWithoutRecipientId() throws NotFoundAccountException, InsufficientBalanceException {
        exception.expect(NotFoundAccountException.class);
        exception.expectMessage("Recipient account not found");

        //Long senderId, Long recipientId, BigDecimal transferValue
        //given a senderId equals 1
        Long sendId = 1L;
        //and a recipientId equals 2
        Long recipient = 2L;
        //and transferValue equals 10
        BigDecimal transferValue = BigDecimal.TEN;

        //and senderId matches with existent sender account
        Account senderAccount = Account.builder().name("Rafael").currentBalance(BigDecimal.TEN).build();;
        when(accountGateway.find(sendId)).thenReturn(senderAccount);
        //and recipientId matches with existent recipient account
        Account recipientAccount = null;
        when(accountGateway.find(recipient)).thenReturn(recipientAccount);

        Transfer transfer = Transfer.builder().id(1L).value(BigDecimal.TEN).
                senderAccountId(senderAccount).
                recipientAccountId(recipientAccount).
                status(TransferStatus.RECEIVED).
                build();
        when(transferGateway.save(any())).thenReturn(transfer);

        //when I try to send this message
        senderTransferGateway.send(sendId, recipient, transferValue);
        //then an exception was invoked

    }

}
