package est.wordwise.domain.security.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "custom.jwt")
public class JwtConfig {

    private final Validation validation;
    private final Secret secret;

    @RequiredArgsConstructor
    @Getter
    public static class Validation {
        private final Duration access;
        private final Duration refresh;
    }

    @RequiredArgsConstructor
    @Getter
    public static class Secret {
        private final String appKey;
        private final String originKey;
    }

}