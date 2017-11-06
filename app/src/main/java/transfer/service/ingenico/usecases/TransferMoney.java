package transfer.service.ingenico.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.domains.Exceptions.InsufficientBalanceException;
import transfer.service.ingenico.domains.Exceptions.NotFoundAccountException;
import transfer.service.ingenico.domains.Transfer;
import transfer.service.ingenico.gateway.AccountGateway;
import transfer.service.ingenico.gateway.TransferGateway;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class TransferMoney {

    private AccountGateway accountGateway;
    private TransferGateway transferGateway;

    @Autowired
    public TransferMoney(AccountGateway accountGateway, TransferGateway transferGateway) {
        this.accountGateway = accountGateway;
        this.transferGateway = transferGateway;
    }

    @Transactional
    public void execute(Long senderId, Long recipientId, BigDecimal transferValue) throws NotFoundAccountException, InsufficientBalanceException {

        Account senderAccount =  Optional.ofNullable(accountGateway.find(senderId)).
                orElseThrow(() -> new NotFoundAccountException("Sender account not found"));

        Account recipientAccount = Optional.ofNullable(accountGateway.find(recipientId)).
                orElseThrow(() -> new NotFoundAccountException("Recipient account not found"));

        Optional.ofNullable(senderAccount).
                filter(account -> account.getCurrentBalance().compareTo(transferValue) >= 0).
                orElseThrow(() -> new InsufficientBalanceException("Insufficient Balance"));

        senderAccount.setCurrentBalance(senderAccount.getCurrentBalance().subtract(transferValue));
        recipientAccount.setCurrentBalance(recipientAccount.getCurrentBalance().add(transferValue));

        transferGateway.save(Transfer.builder().
                senderAccountId(senderAccount).
                recipientAccountId(recipientAccount).
                value(transferValue).
                build());

        accountGateway.saveUpdate(recipientAccount);
        accountGateway.saveUpdate(senderAccount);

    }
}
