package est.wordwise.domain.security.dto;

import lombok.Data;

@Data
public class SigninReq {
    private String email;
    private String password;
}
