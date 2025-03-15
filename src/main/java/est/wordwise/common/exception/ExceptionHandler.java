package est.wordwise.common.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionHandler {
    //member
    public static final String MEMBER_NOT_FOUND_ERROR = "멤버를 찾을 수 없습니다.";
    public static final String INVALID_USERNAME_PASSWORD_ERROR = "이메일 또는 비밀번호가 잘못되었습니다.";
    public static final String REFRESH_TOKEN_NOT_FOUND_ERROR = "리프레시 토큰을 찾을 수 없습니다.";

    //post
    public static final String POST_NOT_FOUND_ERROR = "게시글을 찾을 수 없습니다";
}
