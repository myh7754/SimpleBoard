package est.wordwise.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenNotFoundException extends RuntimeException {
  public TokenNotFoundException() {
  }

  public TokenNotFoundException(String message) {
        super(message);
    }

  public TokenNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public TokenNotFoundException(Throwable cause) {
    super(cause);
  }

  public TokenNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
