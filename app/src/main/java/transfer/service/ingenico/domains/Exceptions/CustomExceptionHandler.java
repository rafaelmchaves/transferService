package transfer.service.ingenico.domains.Exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import transfer.service.ingenico.gateway.http.json.ErrorResponse;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    private static final String EXCEPTION_MESSAGE_LOG_PATTERN = "Request: {} ";
    private static final String EXCEPTION_GENERAL_MESSAGE_LOG_PATTERN = "Request: {} raise {} ";

    @ExceptionHandler(value = NotFoundAccountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundAccountException(HttpServletRequest req, Exception ex) {
        log.info(EXCEPTION_MESSAGE_LOG_PATTERN, req.getRequestURL(), ex);
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse handleInsufficientBalanceException(HttpServletRequest req, Exception ex) {
        log.info(EXCEPTION_MESSAGE_LOG_PATTERN, req.getRequestURL());
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleRuntimeException(HttpServletRequest req, Exception ex) {
        log.info(EXCEPTION_GENERAL_MESSAGE_LOG_PATTERN, req.getRequestURL(), ex);
        return new ErrorResponse(ex.getMessage());
    }
}
