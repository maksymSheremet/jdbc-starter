package my.code.starter.exception;

public class PropertiesException extends RuntimeException {
    public PropertiesException(String message, Exception exception) {
        super(message, exception);
    }
}
