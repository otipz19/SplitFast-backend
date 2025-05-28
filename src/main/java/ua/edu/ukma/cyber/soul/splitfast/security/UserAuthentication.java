package ua.edu.ukma.cyber.soul.splitfast.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
public class UserAuthentication extends UsernamePasswordAuthenticationToken {

    public UserAuthentication(String username, String role) {
        super(username, null, List.of(new SimpleGrantedAuthority(role)));
    }
}
