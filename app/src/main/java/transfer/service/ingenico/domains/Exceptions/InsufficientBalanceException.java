package transfer.service.ingenico.domains.Exceptions;

public class InsufficientBalanceException extends Exception {

    private static final long serialVersionUID = 2493828286868247451L;

    private Object[] msgParams;

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientBalanceException(String message, Object... msgParams) {
        super(message);
        this.msgParams = msgParams;
    }

    public InsufficientBalanceException(String message, Throwable cause, Object[] msgParams) {
        super(message, cause);
        this.msgParams = msgParams;
    }
}
