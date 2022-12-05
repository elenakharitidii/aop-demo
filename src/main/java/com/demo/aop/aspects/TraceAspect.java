package com.demo.aop.aspects;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TraceAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(com.demo.aop..*)")
    public void myApplicationPackage() {
    }

    @Around("myApplicationPackage()")
    public Object trace(final ProceedingJoinPoint joinPoint) throws Throwable {
        final JoinPointShortcuts shortcuts = new JoinPointShortcuts(joinPoint);
        Instant start = Instant.now();

        logger.info("event=enterMethodCall, signature={}(..), arguments={}", shortcuts.signature, shortcuts.arguments);
        try {
            final Object result = joinPoint.proceed();
            Duration duration = Duration.between(start, Instant.now());
            logger.info("event=exitMethodCall, signature={}(..), result={}, durationMillis={}", shortcuts.signature, result, duration.toMillis());
            return result;
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            logger.error("event=exitMethodCallWithException, signature={}(..), arguments={}, exceptionName={}, durationMillis={}",
                    shortcuts.signature, shortcuts.arguments, e.getClass().getSimpleName(), duration.toMillis(), e);
            throw e;
        }
    }

    class JoinPointShortcuts {
        private final String signature;
        private final String arguments;

        public JoinPointShortcuts(ProceedingJoinPoint joinPoint) {
            signature = joinPoint.getSignature().getDeclaringType().getSimpleName() +"."+joinPoint.getSignature().getName();
            arguments = Arrays.toString(joinPoint.getArgs());
        }
    }
}