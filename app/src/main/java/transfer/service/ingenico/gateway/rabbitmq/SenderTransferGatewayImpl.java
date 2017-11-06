package transfer.service.ingenico.gateway.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.domains.Exceptions.NotFoundAccountException;
import transfer.service.ingenico.domains.Transfer;
import transfer.service.ingenico.domains.TransferStatus;
import transfer.service.ingenico.gateway.AccountGateway;
import transfer.service.ingenico.gateway.SenderTransferGateway;
import transfer.service.ingenico.gateway.TransferGateway;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class SenderTransferGatewayImpl implements SenderTransferGateway {

    private AccountGateway accountGateway;

    private TransferGateway transferGateway;

    @Value("${rabbit.transferService.routingKey:transfer.service.event}")
    private String transferServiceRoutingKey;

    @Value("${rabbit.transferService.exchange:transfer-service-event}")
    private String transferServiceExchange;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SenderTransferGatewayImpl(AccountGateway accountGateway, TransferGateway transferGateway, RabbitTemplate rabbitTemplate) {
        this.accountGateway = accountGateway;
        this.transferGateway = transferGateway;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Long senderId, Long recipientId, BigDecimal transferValue) throws NotFoundAccountException {
        Account senderAccount =  Optional.ofNullable(accountGateway.find(senderId)).
                orElseThrow(() -> new NotFoundAccountException("Sender account not found"));

        Account recipientAccount = Optional.ofNullable(accountGateway.find(recipientId)).
                orElseThrow(() -> new NotFoundAccountException("Recipient account not found"));

        Transfer transfer = transferGateway.save(Transfer.builder().
                senderAccountId(senderAccount).
                recipientAccountId(recipientAccount).
                value(transferValue).
                status(TransferStatus.RECEIVED).
                build());

        rabbitTemplate.convertAndSend(transferServiceExchange, transferServiceRoutingKey, transfer.getId());
    }

}
