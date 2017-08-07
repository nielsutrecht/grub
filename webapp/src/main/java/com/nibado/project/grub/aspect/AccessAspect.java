package com.nibado.project.grub.aspect;

import com.nibado.project.grub.access.Access;
import com.nibado.project.grub.access.AccessLevel;
import com.nibado.project.grub.users.repository.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class AccessAspect {
    private static final AccessLevel DEFAULT_ACCESS = AccessLevel.USER;

    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping) && execution(public * *(..))")
    public void restrict(final JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Access annotation = signature.getMethod().getAnnotation(Access.class);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();

        AccessLevel requiredAccess = annotation == null ? DEFAULT_ACCESS : annotation.value();
        AccessLevel actualAccess = getUserLevel(request);

        log.debug("Access annotation: {}, required level: {}", annotation == null ? null : annotation.value(), requiredAccess);
        log.debug("User {} has level {}", ((User) request.getAttribute("user")).getEmail(), actualAccess);

        if (!hasAccess(requiredAccess, actualAccess)) {
            throw new AccessException(String.format("%s %s requires %s access", request.getMethod(), request.getRequestURI(), requiredAccess));
        }
    }

    private static AccessLevel getUserLevel(final HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        if (user == null || !user.isAuthorized()) {
            return AccessLevel.ANONYMOUS;
        } else {
            return user.isAdmin() ? AccessLevel.ADMIN : AccessLevel.USER;
        }
    }

    private static boolean hasAccess(final AccessLevel required, final AccessLevel actual) {
        switch (required) {
            case ANONYMOUS:
                return true;
            case USER:
                return actual == AccessLevel.USER || actual == AccessLevel.ADMIN;
            case ADMIN:
                return actual == AccessLevel.ADMIN;
            default:
                throw new RuntimeException("Unexpected value: " + required);
        }
    }
}
