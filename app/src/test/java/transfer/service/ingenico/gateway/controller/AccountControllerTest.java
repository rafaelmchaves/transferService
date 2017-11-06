package transfer.service.ingenico.gateway.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import transfer.service.ingenico.domains.Exceptions.InsufficientBalanceException;
import transfer.service.ingenico.domains.Exceptions.NotFoundAccountException;
import transfer.service.ingenico.gateway.controller.conf.AbstractHttpTest;
import transfer.service.ingenico.gateway.http.AccountController;
import transfer.service.ingenico.gateway.http.json.AccountRequest;
import transfer.service.ingenico.gateway.http.json.TransferMoneyRequest;
import transfer.service.ingenico.gateway.rabbitmq.SenderTransferGatewayImpl;
import transfer.service.ingenico.usecases.CreateAccount;
import transfer.service.ingenico.usecases.TransferMoney;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AccountController.class)
public class AccountControllerTest extends AbstractHttpTest {
    private static final String API_END_POINT = "/api/v1/account";

    @Autowired
    private AccountController accountController;

    @MockBean
    private CreateAccount createAccount;

    @MockBean
    private TransferMoney transferMoney;

    @MockBean
    private SenderTransferGatewayImpl transferServiceGateway;

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mockMvc = buildMockMvcWithBusinessExecptionHandler(accountController);
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    public void createAccountWithSuccess() throws Exception {

        // Given a account request
        AccountRequest request = AccountRequest.builder().name("Rafael").currentBalance(BigDecimal.TEN).build();
        String json = mapper.writeValueAsString(request);

        // When I try create a new account
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(API_END_POINT).contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

        // Then the request must return a status 201
        Assert.assertThat(mvcResult.getResponse().getStatus(), equalTo(201));
        verify(createAccount, times(1)).execute(any());

    }

    @Test
    public void internalServerErrorWhenCreateAccount() throws Exception {

        // Given a account request
        AccountRequest request = AccountRequest.builder().name("Rafael").currentBalance(BigDecimal.TEN).build();
        String json = mapper.writeValueAsString(request);

        //and a runtime exception
        doThrow(new RuntimeException("Internal Server Error")).when(createAccount).execute(any());
        // When I try create a new account
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(API_END_POINT).contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

        // Then the request must return a status 500
        Assert.assertThat(mvcResult.getResponse().getStatus(), equalTo(500));
        verify(createAccount, times(1)).execute(any());

    }

    @Test
    public void CreateAccountWithoutNameParameter() throws Exception {

        // Given a account request without name parameter
        AccountRequest request = AccountRequest.builder().currentBalance(BigDecimal.TEN).build();
        String json = mapper.writeValueAsString(request);

        // When I try create a new account
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(API_END_POINT).contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

        // Then the request must return a status 400
        Assert.assertThat(mvcResult.getResponse().getStatus(), equalTo(400));
        verify(createAccount, times(0)).execute(any());

    }

    @Test
    public void CreateAccountWithoutBalanceParameter() throws Exception {

        // Given a account request without balance parameter
        AccountRequest request = AccountRequest.builder().name("Rafael").build();
        String json = mapper.writeValueAsString(request);

        // When I try create a new account
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(API_END_POINT).contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

        // Then the request must return a status 400
        Assert.assertThat(mvcResult.getResponse().getStatus(), equalTo(400));
        verify(createAccount, times(0)).execute(any());

    }

    @Test
    public void transferMoneyWithSuccess() throws Exception {

        // Given a transfer money request
        TransferMoneyRequest transferMoneyRequest = TransferMoneyRequest.builder().
                senderId(1L).
                recipientId(2L).
                transferValue(BigDecimal.TEN).
                build();
        String json = mapper.writeValueAsString(transferMoneyRequest);

        // When I try transfer money
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(API_END_POINT).contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

        // Then the request must return a status 201
        Assert.assertThat(mvcResult.getResponse().getStatus(), equalTo(204));
        verify(transferServiceGateway, times(1)).send(transferMoneyRequest.getSenderId(), transferMoneyRequest.getRecipientId(), transferMoneyRequest.getTransferValue());

    }

    @Test
    public void transferMoneyWithoutSenderIdParameter() throws Exception {

        // Given a transfer money request without sender Id parameter
        TransferMoneyRequest transferMoneyRequest = TransferMoneyRequest.builder().
                recipientId(2l).
                transferValue(BigDecimal.TEN).
                build();
        String json = mapper.writeValueAsString(transferMoneyRequest);

        // When I try transfer money
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(API_END_POINT).contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

        // Then the request must return a status 400
        Assert.assertThat(mvcResult.getResponse().getStatus(), equalTo(400));
        verify(transferServiceGateway, times(0)).send(transferMoneyRequest.getSenderId(), transferMoneyRequest.getRecipientId(), transferMoneyRequest.getTransferValue());

    }

    @Test
    public void transferMoneyWithoutRecipientIdParameter() throws Exception {

        // Given a transfer money request without recipient Id parameter
        TransferMoneyRequest transferMoneyRequest = TransferMoneyRequest.builder().
                senderId(1l).
                transferValue(BigDecimal.TEN).
                build();
        String json = mapper.writeValueAsString(transferMoneyRequest);

        // When I try transfer money
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(API_END_POINT).contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

        // Then the request must return a status 400
        Assert.assertThat(mvcResult.getResponse().getStatus(), equalTo(400));
        verify(transferServiceGateway, times(0)).send(transferMoneyRequest.getSenderId(), transferMoneyRequest.getRecipientId(), transferMoneyRequest.getTransferValue());

    }

    @Test
    public void transferMoneyWithoutTransferValueParameter() throws Exception {

        // Given a transfer money request without transfer value parameter
        TransferMoneyRequest transferMoneyRequest = TransferMoneyRequest.builder().
                senderId(1l).
                recipientId(2l).
                build();
        String json = mapper.writeValueAsString(transferMoneyRequest);

        // When I try transfer money
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(API_END_POINT).contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

        // Then the request must return a status 400
        Assert.assertThat(mvcResult.getResponse().getStatus(), equalTo(400));
        verify(transferServiceGateway, times(0)).send(transferMoneyRequest.getSenderId(), transferMoneyRequest.getRecipientId(), transferMoneyRequest.getTransferValue());

    }

    @Test
    public void transferMoneyAccountNotFound() throws Exception {

        // Given a transfer money request
        TransferMoneyRequest transferMoneyRequest = TransferMoneyRequest.builder().
                senderId(1l).
                recipientId(2l).
                transferValue(BigDecimal.TEN).
                build();
        String json = mapper.writeValueAsString(transferMoneyRequest);

        //and sender account is not found
        doThrow(new NotFoundAccountException("Sender Account not found")).when(transferServiceGateway).send(any(), any(), any());
        // When I try transfer money
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(API_END_POINT).contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

        // Then the request must return a status 404
        Assert.assertThat(mvcResult.getResponse().getStatus(), equalTo(404));
        verify(transferServiceGateway, times(1)).send(transferMoneyRequest.getSenderId(), transferMoneyRequest.getRecipientId(), transferMoneyRequest.getTransferValue());

    }

    @Test
    public void InternalServerErrorWhenTransferMoney() throws Exception {

        // Given a transfer money request
        TransferMoneyRequest transferMoneyRequest = TransferMoneyRequest.builder().
                senderId(1l).
                recipientId(2l).
                transferValue(BigDecimal.TEN).
                build();
        String json = mapper.writeValueAsString(transferMoneyRequest);

        //and send balance is insufficient to transfer money
        doThrow(new RuntimeException("Internal Server Error")).when(transferServiceGateway).send(any(), any(), any());
        // When I try transfer money
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(API_END_POINT).contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

        // Then the request must return a status 522
        Assert.assertThat(mvcResult.getResponse().getStatus(), equalTo(500));
        verify(transferServiceGateway, times(1)).send(transferMoneyRequest.getSenderId(), transferMoneyRequest.getRecipientId(), transferMoneyRequest.getTransferValue());

    }
}
