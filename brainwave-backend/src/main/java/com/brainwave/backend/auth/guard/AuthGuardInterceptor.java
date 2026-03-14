package com.brainwave.backend.auth.guard;

import com.brainwave.backend.auth.dto.TokenPrincipal;
import com.brainwave.backend.auth.facade.AuthFacade;
import com.brainwave.core.config.properties.AuthProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AuthGuardInterceptor implements HandlerInterceptor {

    public static final String ATTR_AUTH_PRINCIPAL = "auth.principal";

    private final AuthFacade authFacade;
    private final AuthProperties authProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!authProperties.getGuard().isEnabled()) {
            return true;
        }

        AuthRequirement requirement = resolveRequirement(request.getRequestURI());
        if (requirement == AuthRequirement.NONE) {
            return true;
        }

        String header = request.getHeader("Authorization");
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "缺少或無效 Authorization Header");
        }

        String rawToken = header.substring(7).trim();
        if (!StringUtils.hasText(rawToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token 不可為空");
        }

        TokenPrincipal principal = authFacade.verifyToken(rawToken);
        if (!isScopeAllowed(requirement, principal.scope())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token scope 不足");
        }
        if (!isRoleAllowed(requirement, principal.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "角色權限不足");
        }

        request.setAttribute(ATTR_AUTH_PRINCIPAL, principal);
        return true;
    }

    private AuthRequirement resolveRequirement(String path) {
        if (path == null) {
            return AuthRequirement.NONE;
        }

        if (path.startsWith("/api/users") || path.startsWith("/api/members")) {
            return AuthRequirement.ADMIN;
        }

        if (path.startsWith("/api/system-configs")) {
            return AuthRequirement.ADMIN;
        }

        if (path.startsWith("/api/front/") && !path.startsWith("/api/front/auth")) {
            return AuthRequirement.FRONT;
        }

        return AuthRequirement.NONE;
    }

    private boolean isScopeAllowed(AuthRequirement requirement, String scope) {
        if (!StringUtils.hasText(scope)) {
            return false;
        }
        return switch (requirement) {
            case ADMIN -> scope.startsWith("ADMIN_");
            case FRONT -> scope.startsWith("FRONT_");
            case NONE -> true;
        };
    }

    private boolean isRoleAllowed(AuthRequirement requirement, String role) {
        if (requirement == AuthRequirement.NONE) {
            return true;
        }
        if (!StringUtils.hasText(role)) {
            return false;
        }
        return switch (requirement) {
            case ADMIN -> "ADMIN".equals(role) || "EDITOR".equals(role) || "VIEWER".equals(role);
            case FRONT -> "MEMBER".equals(role);
            case NONE -> true;
        };
    }


    private enum AuthRequirement {
        NONE,
        ADMIN,
        FRONT
    }
}
