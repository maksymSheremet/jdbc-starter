package my.code.starter.exception;

public class ConnectionException extends RuntimeException {
    public ConnectionException(Exception message) {
        super(message);
    }

    public ConnectionException(String message, Exception exception) {
        super(message, exception);
    }
}
