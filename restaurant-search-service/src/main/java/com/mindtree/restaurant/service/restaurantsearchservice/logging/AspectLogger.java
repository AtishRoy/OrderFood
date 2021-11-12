package com.mindtree.restaurant.service.restaurantsearchservice.logging;

import java.util.Arrays;

import org.slf4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectLogger {

    private static Logger logger = LoggerFactory.getLogger(AspectLogger.class);

    @Before("execution(* com.mindtree.restaurant.service.restaurantsearchservice.*.*.*(..))")
    public void before(JoinPoint joinPoint) throws Throwable {
        logger.debug(joinPoint.getSignature().getName());
        logger.debug(Arrays.toString(joinPoint.getArgs()));
    }

    @AfterThrowing(pointcut = "execution(* com.mindtree.restaurant.service.restaurantsearchservice.*.*.*(..))", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error(joinPoint.getSignature().getName(), exception);
        logger.error(exception.getMessage());
        logger.error("Exception :", exception);
    }

    @AfterReturning(pointcut = "execution(* com.mindtree.restaurant.service.restaurantsearchservice.*.*.*(..))", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        logger.debug(joinPoint.getSignature().getName() + "Returned value" + result);
    }

    @After("execution(* com.mindtree.restaurant.service.restaurantsearchservice.*.*.*(..))")
    public void after(JoinPoint joinPoint) {
        logger.debug(joinPoint.getSignature().getName());
        logger.debug(Arrays.toString(joinPoint.getArgs()));
    }
}
