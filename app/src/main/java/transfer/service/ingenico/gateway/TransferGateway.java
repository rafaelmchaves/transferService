package transfer.service.ingenico.gateway;

import transfer.service.ingenico.domains.Transfer;

public interface TransferGateway {
    Transfer save(Transfer transfer);

    Transfer find(Long id);
}
