package com.brainwave.core.audit;

import com.brainwave.core.config.properties.AuditProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 稽核日誌切面：攔截標記了 @Audit 的方法，依設定記錄操作資訊。
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditProperties auditProperties;

    @Around("@annotation(audit)")
    public Object logAround(ProceedingJoinPoint joinPoint, Audit audit) throws Throwable {
        Object result = null;
        Throwable error = null;
        long startTime = System.currentTimeMillis();
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            error = ex;
            throw ex;
        } finally {
            if (!auditProperties.isEnabled()) {
                // 稽核未啟用時直接回傳原始結果
                return result;
            }

            long durationMs = System.currentTimeMillis() - startTime;
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = null;
            if (attrs instanceof ServletRequestAttributes servletAttrs) {
                request = servletAttrs.getRequest();
            }

            String path = request != null ? request.getRequestURI() : "";
            String method = request != null ? request.getMethod() : "";

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
            String action = audit.action();
            String resource = !audit.resource().isEmpty() ? audit.resource() : methodName;
            boolean success = (error == null);

            log.info(
                    "AUDIT action={} resource={} method={} path={} success={} durationMs={}",
                    action,
                    resource,
                    method,
                    path,
                    success,
                    durationMs
            );
        }
    }
}

