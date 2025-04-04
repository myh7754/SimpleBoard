package est.wordwise.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RefreshTokenNotFoundException extends RuntimeException {
  public RefreshTokenNotFoundException() {
    super();
  }

  public RefreshTokenNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public RefreshTokenNotFoundException(Throwable cause) {
    super(cause);
  }

  protected RefreshTokenNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public RefreshTokenNotFoundException(String message) {
    super(message);
  }
}
