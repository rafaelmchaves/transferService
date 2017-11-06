package transfer.service.ingenico.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.domains.Exceptions.InsufficientBalanceException;
import transfer.service.ingenico.domains.Exceptions.NotFoundAccountException;
import transfer.service.ingenico.domains.Transfer;
import transfer.service.ingenico.domains.TransferStatus;
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
    public void execute(Long transferId) throws NotFoundAccountException, InsufficientBalanceException {

        Transfer transfer = transferGateway.find(transferId);

        Account senderAccount =  Optional.ofNullable(accountGateway.find(transfer.getSenderAccountId().getId())).
                orElseThrow(() ->
                {
                    transfer.setStatus(TransferStatus.ACCOUNT_NOT_FOUND);
                    transferGateway.save(transfer);
                    return new NotFoundAccountException("Sender account not found");
                });

        Account recipientAccount = Optional.ofNullable(accountGateway.find(transfer.getRecipientAccountId().getId())).
                orElseThrow(() ->
                {
                    transfer.setStatus(TransferStatus.ACCOUNT_NOT_FOUND);
                    transferGateway.save(transfer);
                    return new NotFoundAccountException("Recipient account not found");
                });

        verifyAccountsBalance(transfer, senderAccount);

        calculateNewBalance(transfer, senderAccount, recipientAccount);

        transfer.setStatus(TransferStatus.SUCCESS);
        transferGateway.save(transfer);

        accountGateway.saveUpdate(recipientAccount);
        accountGateway.saveUpdate(senderAccount);

    }

    private void verifyAccountsBalance(Transfer transfer, Account senderAccount) throws InsufficientBalanceException {
        Optional.ofNullable(senderAccount).
                filter(account -> account.getCurrentBalance().compareTo(transfer.getValue()) >= 0).
                orElseThrow(() ->
                {
                    transfer.setStatus(TransferStatus.INSUFFICIENT_BALANCE);
                    transferGateway.save(transfer);
                   return new InsufficientBalanceException("Sender have an insufficient balance to transfer");
                });
    }

    private void calculateNewBalance(Transfer transfer, Account senderAccount, Account recipientAccount) {
        senderAccount.setCurrentBalance(senderAccount.getCurrentBalance().subtract(transfer.getValue()));
        recipientAccount.setCurrentBalance(recipientAccount.getCurrentBalance().add(transfer.getValue()));
    }
}
