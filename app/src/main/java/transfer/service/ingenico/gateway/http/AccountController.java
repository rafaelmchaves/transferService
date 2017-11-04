package transfer.service.ingenico.gateway.http;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.gateway.http.json.AccountRequest;
import transfer.service.ingenico.usecases.CreateAccount;
import transfer.service.ingenico.usecases.TransferMoney;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/account")
@Api(value = "/api/v1/account", description = "Rest API for account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {


    private CreateAccount createAccount;
    private TransferMoney transferMoney;

    @Autowired
    public AccountController(CreateAccount createAccount, TransferMoney transferMoney) {
        this.createAccount = createAccount;
        this.transferMoney = transferMoney;
    }

    // @formatter:off
    @ApiOperation(value = "Create a new account")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Account created"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    // @formatter:on
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody @Valid AccountRequest accountRequest) {
        createAccount.execute(Account.builder().
                name(accountRequest.getName()).
                currentBalance(accountRequest.getCurrentBalance()).
                build());
    }

    // @formatter:off
    @ApiOperation(value = "Transfer money from an account to other account")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Account created"),
            @ApiResponse(code = 404, message = "Some account don't exist"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "senderId", value = "Sender account number", required = true, dataType = "Long", paramType = "Query"),
            @ApiImplicitParam(name = "recipientId", value = "Recipient account number", required = true, dataType = "Long", paramType = "Query"),
            @ApiImplicitParam(name = "transferValue", value = "Transfer value. This value must to be positive", required = true, dataType = "BigDecimal", paramType = "Query")
    })
    // @formatter:on
    @RequestMapping(method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@NotNull(message = "{msg.validation.senderId.null}") Long senderId,
                              @NotNull(message = "{msg.validation.recipientId.null}") Long recipientId,
                              @NotNull(message = "{msg.validation.transferValue.null}") BigDecimal transferValue) {
        transferMoney.execute(senderId,recipientId, transferValue);
    }
}
