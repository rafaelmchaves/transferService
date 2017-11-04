package transfer.service.ingenico.gateway;

import transfer.service.ingenico.domains.Transfer;

public interface TransferGateway {
    void save(Transfer transfer);
}
