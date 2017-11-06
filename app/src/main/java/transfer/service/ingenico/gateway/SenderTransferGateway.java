package transfer.service.ingenico.gateway;

import transfer.service.ingenico.domains.Exceptions.NotFoundAccountException;

import java.math.BigDecimal;

public interface SenderTransferGateway {
    void send(Long senderId, Long recipientId, BigDecimal transferValue) throws NotFoundAccountException;
}
