package ua.edu.ukma.cyber.soul.splitfast.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.configuration.SecurityConstants;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final SecurityConstants securityConstants;

    public String generateToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date(TimeUtils.getCurrentTimeUTC()))
                .withExpiresAt(new Date(TimeUtils.getCurrentTimeUTC() + securityConstants.getTokenExpiration().toMillis()))
                .withClaim(SecurityConstants.ROLE_CLAIM, role)
                .sign(HMAC512(securityConstants.getTokenSecret().getBytes()));
    }

    public VerificationResult verifyToken(String token) throws JWTVerificationException {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC512(securityConstants.getTokenSecret().getBytes()))
                .withClaimPresence(SecurityConstants.ROLE_CLAIM)
                .build()
                .verify(removePrefix(token));
        return new VerificationResult(jwt.getSubject(), jwt.getClaim(SecurityConstants.ROLE_CLAIM).asString());
    }

    public String generateRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date(TimeUtils.getCurrentTimeUTC()))
                .withExpiresAt(new Date(TimeUtils.getCurrentTimeUTC() + securityConstants.getRefreshTokenExpiration().toMillis()))
                .sign(HMAC512(securityConstants.getTokenSecret().getBytes()));
    }

    public VerificationResult verifyRefreshToken(String token) throws JWTVerificationException {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC512(securityConstants.getTokenSecret().getBytes()))
                .build()
                .verify(removePrefix(token));
        return new VerificationResult(jwt.getSubject(), null);
    }

    private String removePrefix(String token) {
        return token.replace(securityConstants.getTokenPrefix(), "");
    }

    public record VerificationResult(String username, String role) {}
}
