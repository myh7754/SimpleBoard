package est.wordwise.common.exception;

public class ChatRoomNotFoundException extends RuntimeException {
  public ChatRoomNotFoundException() {

  }

  public ChatRoomNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ChatRoomNotFoundException(Throwable cause) {
    super(cause);
  }

  protected ChatRoomNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ChatRoomNotFoundException(String message) {
    super(message);
  }
}
