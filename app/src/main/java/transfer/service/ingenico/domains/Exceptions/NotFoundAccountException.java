package transfer.service.ingenico.domains.Exceptions;

public class NotFoundAccountException extends Exception {

    private static final long serialVersionUID = 2493828286868247451L;

    private Object[] msgParams;

    public NotFoundAccountException(String message) {
        super(message);
    }

    public NotFoundAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundAccountException(String message, Object... msgParams) {
        super(message);
        this.msgParams = msgParams;
    }

    public NotFoundAccountException(String message, Throwable cause, Object[] msgParams) {
        super(message, cause);
        this.msgParams = msgParams;
    }
}
