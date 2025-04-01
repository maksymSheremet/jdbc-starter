package my.code.starter.jdbc_core.exception;

public class PropertiesException extends RuntimeException {
    public PropertiesException(String message, Exception exception) {
        super(message, exception);
    }
}
