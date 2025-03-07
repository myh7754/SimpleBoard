package est.wordwise.domain.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeyPair {
    private String accessToken;
    private String refreshToken;
}
