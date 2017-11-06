package transfer.service.ingenico.integrationTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.domains.Transfer;
import transfer.service.ingenico.gateway.AccountRepository;
import transfer.service.ingenico.gateway.TransferRepository;
import transfer.service.ingenico.gateway.http.json.TransferMoneyRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferMoneyIntegrationTest {

    private static final String URL = "/api/v1/account";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void transferMoneyWithSuccess() {

        //given a sender account whose own is "Rafael" that have current balance equals 20
        Account senderAccount = Account.builder().name("Rafael").currentBalance(BigDecimal.valueOf(20L)).build();
        accountRepository.save(senderAccount);
        //and a recipient account whose own is "Amanda" that have current balance equals 1
        Account recipientAccount = Account.builder().name("Amanda").currentBalance(BigDecimal.valueOf(1L)).build();
        accountRepository.save(recipientAccount);

        //when sender try do a transfer of 10 euros to recipient
        TransferMoneyRequest transferMoneyRequest = TransferMoneyRequest.builder().
                senderId(1l).
                recipientId(2l).
                transferValue(BigDecimal.TEN).
                build();

        restTemplate.postForEntity("/api/v1/account?_method=patch", transferMoneyRequest, void.class);

        //then the sender balance is 10 euros
        Account updatedSenderAccount = accountRepository.findOne(1L);
        assertEquals(updatedSenderAccount.getCurrentBalance().setScale(2), BigDecimal.TEN.setScale(2));
        //and the recipient balance is 11 euros
        Account updatedRecipientAccount = accountRepository.findOne(2L);
        assertEquals(updatedRecipientAccount.getCurrentBalance().setScale(2), BigDecimal.valueOf(11L).setScale(2));
        //and that transference was recorded on DB
        List<Transfer> transferMoneyList = transferRepository.findAll();
        assertEquals(transferMoneyList.size(), 1);
        Transfer transfer = transferMoneyList.stream().findFirst().get();
        assertEquals(transfer.getValue().setScale(2), transferMoneyRequest.getTransferValue().setScale(2));
        assertEquals(transfer.getSenderAccountId().getName(), updatedSenderAccount.getName());
        assertEquals(transfer.getRecipientAccountId().getName(), updatedRecipientAccount.getName());
        assertEquals(transfer.getSenderAccountId().getCurrentBalance().setScale(2), updatedSenderAccount.getCurrentBalance().setScale(2));
        assertEquals(transfer.getRecipientAccountId().getCurrentBalance().setScale(2), updatedRecipientAccount.getCurrentBalance().setScale(2));

    }

    @Test
    public void testSimultaneousTransferRequests() throws InterruptedException {
        //given a sender account whose own is "Rafael" that have current balance equals 20
        Account senderAccount = Account.builder().name("Rafael").currentBalance(BigDecimal.valueOf(20L)).build();
        accountRepository.save(senderAccount);
        //and a recipient account whose own is "Amanda" that have current balance equals 1
        Account recipientAccount = Account.builder().name("Amanda").currentBalance(BigDecimal.valueOf(1L)).build();
        accountRepository.save(recipientAccount);

        //when sender try do five transfers of 10 euros to recipient
        TransferMoneyRequest transferMoneyRequest = TransferMoneyRequest.builder().
                senderId(1L).
                recipientId(2L).
                transferValue(BigDecimal.TEN).
                build();

        List<TransferMoneyRequest> transferMoneyRequestList = new ArrayList<>();
        transferMoneyRequestList.add(transferMoneyRequest);
        transferMoneyRequestList.add(transferMoneyRequest);
        transferMoneyRequestList.add(transferMoneyRequest);
        transferMoneyRequestList.add(transferMoneyRequest);
        transferMoneyRequestList.add(transferMoneyRequest);

        transferMoneyRequestList.stream().forEach(transfer -> {
            Runnable runnable = () -> {
                restTemplate.postForEntity("/api/v1/account?_method=patch", transfer, void.class);
            };
            Thread thread = new Thread(runnable);
            thread.start();
        });

        Thread.sleep(6000);
        //Then only 2 transfers will be recorded
        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(transferList.size(), 2);
        //and sender account balance will have 0.00 euros
        assertEquals(accountRepository.findOne(1l).getCurrentBalance().setScale(2), BigDecimal.ZERO.setScale(2));
        //and recipient account balance will have 0.00 euros
        assertEquals(accountRepository.findOne(2l).getCurrentBalance().setScale(2), BigDecimal.valueOf(21L).setScale(2));

    }
}