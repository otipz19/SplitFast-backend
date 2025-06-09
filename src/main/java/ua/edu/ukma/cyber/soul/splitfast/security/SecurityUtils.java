package ua.edu.ukma.cyber.soul.splitfast.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.UnauthenticatedException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.EnumsMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;

@Component
@RequestScope
@RequiredArgsConstructor
public class SecurityUtils {

    private final EnumsMapper enumsMapper;
    private final UserRepository userRepository;

    private UserRole userRole;
    private UserEntity currentUser;

    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth instanceof UserAuthentication;
    }

    public void authenticated() {
        if (!isAuthenticated())
            throw new UnauthenticatedException();
    }

    public UserRole getUserRole() {
        if (userRole == null)
            userRole = loadRole();
        return userRole;
    }

    private UserRole loadRole() {
        authenticated();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return enumsMapper.fromString(auth.getAuthorities().iterator().next().getAuthority());
    }

    public boolean hasRole(UserRole... roles) {
        UserRole role = getUserRole();
        for (UserRole r : roles) {
            if (role == r)
                return true;
        }
        return false;
    }

    public void requireRole(UserRole... roles) {
        if (!hasRole(roles))
            throw new ForbiddenException();
    }

    public UserEntity getCurrentUser() {
        if (currentUser == null) {
            currentUser = loadUser();
            userRole = currentUser.getRole();
        }
        return currentUser;
    }

    private UserEntity loadUser() {
        authenticated();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName()).orElseThrow();
    }

    public int getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
