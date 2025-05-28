package ua.edu.ukma.cyber.soul.splitfast.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.configuration.SecurityConstants;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.mappers.EnumsMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SecurityConstants securityConstants;
    private final EnumsMapper enumsMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        String passwordHash;
        if (securityConstants.getAdminLogin().equals(username))
            passwordHash = securityConstants.getAdminPasswordHash();
        else
            passwordHash = user.getPasswordHash();
        String role = enumsMapper.toString(user.getRole());
        return new User(username, passwordHash, List.of(new SimpleGrantedAuthority(role)));
    }
}
