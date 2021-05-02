package com.example.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class GeneralLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @AfterThrowing(
            value = "execution (* *.*.*(..))",
            throwing = "exc")
    public void logException(JoinPoint joinPoint, Throwable exc) {
        String source = joinPoint.getSignature().getDeclaringTypeName();
        String point = joinPoint.getSignature().getName();

        logger.error("During the execution of " + source + "#" + point + " exception " + exc + " was thrown.");
    }

}
