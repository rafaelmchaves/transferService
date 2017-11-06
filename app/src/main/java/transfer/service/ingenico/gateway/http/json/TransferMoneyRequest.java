package transfer.service.ingenico.gateway.http.json;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransferMoneyRequest {

    @ApiModelProperty(name = "name", value = "Sender account number", required = true, dataType = "Long")
    @NotNull(message = "{msg.validation.senderId.null}")
    private Long senderId;

    @ApiModelProperty(name = "name", value = "Recipient account number", required = true, dataType = "Long")
    @NotNull(message = "{msg.validation.recipientId.null}")
    private Long recipientId;

    @ApiModelProperty(name = "name", value = "Transfer value. This value must to be positive", required = true, dataType = "BigDecimal")
    @NotNull(message = "{msg.validation.transferValue.null}")
    private BigDecimal transferValue;
}
