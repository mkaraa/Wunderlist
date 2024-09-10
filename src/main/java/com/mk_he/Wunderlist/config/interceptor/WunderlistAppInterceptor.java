package com.mk_he.Wunderlist.config.interceptor;

import com.mk_he.Wunderlist.config.security.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
public class WunderlistAppInterceptor implements HandlerInterceptor {
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    private final Logger logger = LoggerFactory.getLogger(WunderlistAppInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        String username = SecurityContext.getCurrentUsername();
        String requestUri = request.getRequestURI();
        String method = request.getMethod();

        if (correlationId == null) {
            correlationId = java.util.UUID.randomUUID().toString();
        }
        MDC.put(CORRELATION_ID_HEADER, correlationId);

        // Log request details
        logger.info("Incoming request: CorrelationId - {}, Username - {}, Method - {}, URI - {}",
                correlationId, username, method, requestUri);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String correlationId = MDC.get(CORRELATION_ID_HEADER);

        int status = response.getStatus();
        String method = request.getMethod();
        String requestUri = request.getRequestURI();

        logger.info("Completed request: CorrelationId - {}, Method - {}, URI - {}, Status - {}",
                correlationId, method, requestUri, status);

        if (ex != null) {
            logger.error("Request failed: CorrelationId - {}, Method - {}, URI - {}, Error - {}",
                    correlationId, method, requestUri, ex.getMessage());
        }
    }
}
