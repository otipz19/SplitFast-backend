package ua.edu.ukma.cyber.soul.splitfast.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;

@Component
@RequestScope
public class SecurityUtils {

    public void authorized() {}

    public boolean hasRole(UserRole... roles) { return true; }

    public void requireRole(UserRole... roles) {
        if (!hasRole(roles))
            throw new ForbiddenException();
    }
}
