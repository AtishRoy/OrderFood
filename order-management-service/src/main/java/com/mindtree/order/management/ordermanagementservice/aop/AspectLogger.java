package com.mindtree.order.management.ordermanagementservice.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectLogger {
	
	private Logger logger;
	
	@Before("execution(* com.mindtree.order.management.ordermanagementservice.*.*.*(..))")
	public void before(JoinPoint joinPoint) throws Throwable {
		logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
		logger.debug(joinPoint.getSignature().getName());
		logger.debug(Arrays.toString(joinPoint.getArgs()));
	}
	
	@AfterThrowing(pointcut = "execution(* com.mindtree.order.management.ordermanagementservice.*.*.*(..))",
			throwing = "exception")
	public void afterThrowing(JoinPoint joinPoint, Throwable exception) {
		logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
		logger.error(joinPoint.getSignature().getName(), exception.getMessage());
		logger.error("Exception :", exception);
	}

	@AfterReturning(pointcut = "execution(* com.mindtree.order.management.ordermanagementservice.*.*.*(..))",
			returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
		logger.debug(joinPoint.getSignature().getName() + "Returned value" + result);
	}
	
	@After("execution(* com.mindtree.order.management.ordermanagementservice.*.*.*(..))")
	public void after(JoinPoint joinPoint) {
		logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
		logger.debug(joinPoint.getSignature().getName());
		logger.debug(Arrays.toString(joinPoint.getArgs()));
	}
}
