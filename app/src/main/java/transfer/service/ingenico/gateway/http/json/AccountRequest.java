package transfer.service.ingenico.gateway.http.json;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest implements Serializable {

    private static final long serialVersionUID = 4805751843390298375L;

    @ApiModelProperty(name = "name", value = "Name of person will create a new account", required = true, dataType = "String")
    @NotNull(message = "{msg.validation.account.name.null}")
    private String name;

    @ApiModelProperty(name = "currentBalance", value = "Initial balance", required = true, dataType = "Number")
    @NotNull(message = "{msg.validation.balance.null}")
    private BigDecimal currentBalance;

}
