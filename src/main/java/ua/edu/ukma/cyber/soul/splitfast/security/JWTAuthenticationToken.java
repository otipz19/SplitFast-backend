package ua.edu.ukma.cyber.soul.splitfast.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.io.Serial;

@Getter
public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = -8691636031126328365L;

    private final String token;

    public JWTAuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
