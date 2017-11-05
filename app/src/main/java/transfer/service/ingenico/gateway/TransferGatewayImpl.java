package transfer.service.ingenico.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import transfer.service.ingenico.domains.Transfer;

@Component
public class TransferGatewayImpl implements TransferGateway {

    private TransferRepository repository;

    @Autowired
    public TransferGatewayImpl(TransferRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Transfer transfer) {
        repository.save(transfer);
    }
}
