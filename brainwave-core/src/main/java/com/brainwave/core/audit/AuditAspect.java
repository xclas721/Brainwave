package com.brainwave.core.audit;

import com.brainwave.core.config.properties.AuditProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 稽核日誌切面：攔截標記了 @Audit 的方法，依設定記錄操作資訊。
 * 若有登入，會從 request attribute {@value #ATTR_AUTH_PRINCIPAL} 取 principal；
 * requestId 來自 MDC（由 CorrelationIdFilter 等寫入）。
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    /** 與 AuthGuardInterceptor 使用的 request attribute key 一致，供稽核取「誰」 */
    public static final String ATTR_AUTH_PRINCIPAL = "auth.principal";

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
            Object principalAttr = request != null ? request.getAttribute(ATTR_AUTH_PRINCIPAL) : null;
            String principal = principalAttr != null ? principalAttr.toString() : "";
            String requestId = MDC.get("requestId");

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
            String action = audit.action();
            String resource = !audit.resource().isEmpty() ? audit.resource() : methodName;
            boolean success = (error == null);

            log.info(
                    "AUDIT action={} resource={} method={} path={} success={} durationMs={} requestId={} principal={}",
                    action,
                    resource,
                    method,
                    path,
                    success,
                    durationMs,
                    requestId != null ? requestId : "",
                    principal
            );
        }
    }
}

