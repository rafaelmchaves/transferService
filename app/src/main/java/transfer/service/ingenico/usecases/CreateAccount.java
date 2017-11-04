package transfer.service.ingenico.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.gateway.AccountGateway;

@Component
public class CreateAccount {

    private AccountGateway accountGateway;

    @Autowired
    public CreateAccount(AccountGateway accountGateway) {
        this.accountGateway = accountGateway;
    }

    @Transactional
    public void execute(Account account) {
        accountGateway.saveUpdate(account);
    }
}
