package transfer.service.ingenico.gateway.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import transfer.service.ingenico.domains.Exceptions.InsufficientBalanceException;
import transfer.service.ingenico.domains.Exceptions.NotFoundAccountException;
import transfer.service.ingenico.usecases.TransferMoney;

@Component
@Slf4j
public class TransferListener {

    private TransferMoney transferMoney;

    @Autowired
    public TransferListener(TransferMoney transferMoney) {
        this.transferMoney = transferMoney;
    }

    @RabbitListener(queues = "${rabbit.transferService.queue:ha.transfer.service.event.queue}", containerFactory = "transferServiceListenerContainerFactory")
    public void transfer(Long transferId) throws NotFoundAccountException, InsufficientBalanceException {
        log.info("transferId: " + transferId);
        transferMoney.execute(transferId);
    }

}
