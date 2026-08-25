package com.akshay.moneymanager.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution of controllers and services.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut that matches all Spring MVC controllers.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || execution(* com.akshay.moneymanager.controller..*.*(..))")
    public void controllerPointcut() {
        // Pointcut definition
    }

    /**
     * Pointcut that matches all Spring Services.
     */
    @Pointcut("within(@org.springframework.stereotype.Service *) || execution(* com.akshay.moneymanager.service..*.*(..))")
    public void servicePointcut() {
        // Pointcut definition
    }

    /**
     * Around advice that logs when a request enters and exits a controller or service method.
     */
    @Around("controllerPointcut() || servicePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("=> Entering: {}.{}()", className, methodName);
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("<= Exiting: {}.{}() - Completed in {} ms", className, methodName, elapsedTime);
            return result;
        } catch (Throwable throwable) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.error("<= Exiting with Error: {}.{}() - Failed in {} ms. Error: {}", 
                     className, methodName, elapsedTime, throwable.getMessage());
            throw throwable;
        }
    }
}
