package my.code.starter.jdbc_core.exception;

public class ConnectionException extends RuntimeException {
    public ConnectionException(Exception message) {
        super(message);
    }

    public ConnectionException(String message, Exception exception) {
        super(message, exception);
    }
}
