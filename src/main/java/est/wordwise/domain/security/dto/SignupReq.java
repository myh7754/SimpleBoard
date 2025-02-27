package est.wordwise.domain.security.dto;

import lombok.Data;

@Data
public class SignupReq {
    public String nickname;
    public String password;
    public String email;

}
