package com.example.demo.aop;

import com.example.demo.model.persistence.User;
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
public class CreateUserLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public com.example.demo.model.persistence.User com.example.demo.service.UserServiceImpl.createNewUser(..))")
    public void createUser() {}

    @AfterReturning(
            value = "createUser()",
            returning = "createdUser")
    public void afterSuccessfulUserCreation(JoinPoint joinPoint, Object createdUser) {
        String username = (String) joinPoint.getArgs()[0];
        User user = (User) createdUser;
        logger.info("User " + user + " successfully created for username " + username + ".");
    }

    @AfterThrowing(
            value = "createUser()",
            throwing = "exc"
    )
    public void afterUnsuccessfulUserCreation(JoinPoint joinPoint, RuntimeException exc) {
        String username = (String) joinPoint.getArgs()[0];
        logger.error("Creation of user with username " + username + " ended with exception " + exc + ".");
    }

}
