package transfer.service.ingenico.gateway.http.json;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransferMoneyRequest {

    @NotNull(message = "{msg.validation.senderId.null}")
    private Long senderId;
    @NotNull(message = "{msg.validation.recipientId.null}")
    private Long recipientId;
    @NotNull(message = "{msg.validation.transferValue.null}")
    private BigDecimal transferValue;
}
