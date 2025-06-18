package com.book.demo.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {}

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void service() {}

    @Around("controller() || service()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        String params = Arrays.toString(args);
        String requestInfo = "";
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                requestInfo = String.format("[Request] %s %s", request.getMethod(), request.getRequestURI());
            }
        } catch (Exception ignored) {
            // Intentionally ignored: request info may not be available in all contexts (e.g., service layer)
        }
        logger.info("{} - Entering {}.{} with arguments: {}", requestInfo, className, methodName, params);
        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            String response = serializeResponse(result);
            logger.info("{} - Exiting {}.{}; Response: {}; Execution time: {} ms", requestInfo, className, methodName, response, elapsed);
            return result;
        } catch (Throwable ex) {
            // Already logging and rethrowing for global error handling
            logger.error("{} - Exception in {}.{}: {}", requestInfo, className, methodName, ex.getMessage(), ex);
            if (ex instanceof com.book.demo.exception.BookNotFoundException) {
                throw ex;
            }
            throw new LoggingAspectException(String.format("Exception in %s.%s: %s", className, methodName, ex.getMessage()), ex);
        }
    }

    /**
     * Serializes the response object to JSON string for logging.
     */
    private String serializeResponse(Object result) {
        try {
            if (result instanceof ResponseEntity) {
                return objectMapper.writeValueAsString(((ResponseEntity<?>) result).getBody());
            } else {
                return objectMapper.writeValueAsString(result);
            }
        } catch (Exception e) {
            return result != null ? result.toString() : "null";
        }
    }
}
