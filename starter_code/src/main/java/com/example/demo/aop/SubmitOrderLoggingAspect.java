package com.example.demo.aop;

import com.example.demo.model.persistence.UserOrder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SubmitOrderLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public com.example.demo.model.persistence.UserOrder com.example.demo.controller.OrderController.submit(..))")
    public void submitOrder() {}

    @AfterReturning(
            value = "submitOrder()",
            returning = "submittedOrder"
    )
    public void afterSuccessfulOrderSubmission(Object submittedOrder) {
        UserOrder order = (UserOrder) submittedOrder;
        logger.info("Order for the user " + order.getUser() + " with the price " + order.getTotal() + " was successfully submitted.");
    }

    @AfterThrowing(
            value = "submitOrder()",
            throwing = "exc"
    )
    public void afterUnsuccessfulOrderSubmission(JoinPoint joinPoint, RuntimeException exc) {
        String username = (String) joinPoint.getArgs()[0];
        logger.error("Submission of an order of the user " + username + " ended with exception " + exc + ".");
    }
}
