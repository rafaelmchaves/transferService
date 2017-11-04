package transfer.service.ingenico.gateway.h2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.gateway.AccountGateway;

@Component
public class AccountGatewayImpl implements AccountGateway {

    private AccountRepository repository;

    @Autowired
    public AccountGatewayImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveUpdate(Account account) {
        repository.save(account);
    }

    @Override
    public Account find(Long id) {
        return repository.findOne(id);
    }

}
