package transfer.service.ingenico.gateway.h2;

import org.springframework.stereotype.Component;
import transfer.service.ingenico.domains.Transfer;
import transfer.service.ingenico.gateway.TransferGateway;

@Component
public class TransferGatewayImpl implements TransferGateway {

    private TransferRepository repository;

    public TransferGatewayImpl(TransferRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Transfer transfer) {
        repository.save(transfer);
    }
}
