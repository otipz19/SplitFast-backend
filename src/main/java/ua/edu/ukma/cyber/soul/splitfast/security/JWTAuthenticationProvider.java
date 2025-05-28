package ua.edu.ukma.cyber.soul.splitfast.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class JWTAuthenticationProvider implements AuthenticationProvider {

    private final JWTService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof JWTAuthenticationToken jwtAuthenticationToken) {
            try {
                JWTService.VerificationResult verificationResult = jwtService.verifyToken(jwtAuthenticationToken.getToken());
                return new UserAuthentication(verificationResult.username(), verificationResult.role());
            } catch (Exception e) { /* ignore */ }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
