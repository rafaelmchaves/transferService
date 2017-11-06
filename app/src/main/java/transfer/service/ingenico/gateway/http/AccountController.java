package transfer.service.ingenico.gateway.http;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import transfer.service.ingenico.domains.Account;
import transfer.service.ingenico.domains.Exceptions.InsufficientBalanceException;
import transfer.service.ingenico.domains.Exceptions.NotFoundAccountException;
import transfer.service.ingenico.gateway.rabbitmq.SenderTransferGatewayImpl;
import transfer.service.ingenico.gateway.http.json.AccountRequest;
import transfer.service.ingenico.gateway.http.json.TransferMoneyRequest;
import transfer.service.ingenico.usecases.CreateAccount;
import transfer.service.ingenico.usecases.TransferMoney;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
@Api(value = "/api/v1/account", description = "Rest API for account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private CreateAccount createAccount;
    private TransferMoney transferMoney;
    private SenderTransferGatewayImpl transferServiceGateway;

    @Autowired
    public AccountController(CreateAccount createAccount, TransferMoney transferMoney, SenderTransferGatewayImpl transferServiceGateway) {
        this.createAccount = createAccount;
        this.transferMoney = transferMoney;
        this.transferServiceGateway = transferServiceGateway;
    }

    // @formatter:off
    @ApiOperation(value = "Create a new account")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Account created"),
            @ApiResponse(code = 400, message = "Bad request"),
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
            @ApiResponse(code = 204, message = "Transfer done"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Some Account doesn't exist"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    // @formatter:on
    @RequestMapping(method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@RequestBody @Valid TransferMoneyRequest transferMoneyRequest) throws NotFoundAccountException, InsufficientBalanceException {
        transferServiceGateway.send(transferMoneyRequest.getSenderId(), transferMoneyRequest.getRecipientId(), transferMoneyRequest.getTransferValue());
    }

}
