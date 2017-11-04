package transfer.service.ingenico.gateway;

import transfer.service.ingenico.domains.Account;

public interface AccountGateway {

    void saveUpdate(Account account);
    Account find(Long id);
}
