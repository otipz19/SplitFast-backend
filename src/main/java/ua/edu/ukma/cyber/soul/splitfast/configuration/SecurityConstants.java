package ua.edu.ukma.cyber.soul.splitfast.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Component
public class SecurityConstants {

    public final static String ROLE_CLAIM = "role";

    @Value("${security.token.prefix}")
    private String tokenPrefix;

    @Value("${security.token.secret}")
    private String tokenSecret;

    @Value("${security.token.expiration}")
    private Duration tokenExpiration;

    @Value("${security.refresh-token.expiration}")
    private Duration refreshTokenExpiration;

    @Value("${security.admin.login}")
    private String adminLogin;

    @Value("${security.admin.password-hash}")
    private String adminPasswordHash;
}
