package est.wordwise.domain.security.dto;

import lombok.Data;

@Data
public class SignupRequest {
    public String nickname;
    public String password;
    public String email;

}
