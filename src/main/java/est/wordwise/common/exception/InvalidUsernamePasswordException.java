package est.wordwise.common.exception;

public class InvalidUsernamePasswordException extends RuntimeException {
    public InvalidUsernamePasswordException(Object p0) {
    }

    public InvalidUsernamePasswordException() {
        super();
    }

    public InvalidUsernamePasswordException(String message) {
        super(message);
    }

    public InvalidUsernamePasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUsernamePasswordException(Throwable cause) {
        super(cause);
    }

    protected InvalidUsernamePasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
