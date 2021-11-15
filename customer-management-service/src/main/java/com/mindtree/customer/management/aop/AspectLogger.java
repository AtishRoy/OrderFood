package com.mindtree.customer.management.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectLogger {

	private Logger logger;

	@Before("execution(* com.mindtree.customer.management.*.*.*(..))")
	public void before(JoinPoint joinpoint) throws Throwable {
	    logger = LoggerFactory.getLogger(joinpoint.getSignature().getDeclaringType());
		logger.debug(joinpoint.getSignature().getName());
		logger.debug(Arrays.toString(joinpoint.getArgs()));
	}
	@After("execution(* com.mindtree.customer.management.*.*.*(..))")
	public void after(JoinPoint joinpoint) throws Throwable {
	    logger = LoggerFactory.getLogger(joinpoint.getSignature().getDeclaringType());
		logger.debug(joinpoint.getSignature().getName());
		logger.debug(Arrays.toString(joinpoint.getArgs()));
	}
	@AfterThrowing(value = "execution(* com.mindtree.customer.management.*.*.*(..))",
			throwing = "throwable")
	public void afterThrowing(JoinPoint joinPoint, Throwable throwable) throws Throwable {
	    logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
		logger.error(joinPoint.getSignature().getName(), throwable.getMessage());
		//logger.error(throwable.getMessage());
		logger.error("Exception", throwable);
	}
	@AfterReturning(value = "execution(* com.mindtree.customer.management.*.*.*(..))",
			returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) throws Throwable {
	    logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
		logger.debug(joinPoint.getSignature().getName() + "Returned value" + result);
	}
}
