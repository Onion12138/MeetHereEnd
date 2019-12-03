package com.ecnu.meethere.common.aspect;

import com.ecnu.meethere.common.result.CommonResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Aspect
@Component
public class BindingResultAspect {
    @Around("execution(public * com.ecnu.meethere.*.controller.*.*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult result = (BindingResult) arg;
                if (result.hasErrors()) {
                    FieldError fieldError = result.getFieldError();
                    if (fieldError != null) {
                        return CommonResult.dataBindingFailed()
                                .message(fieldError.getField() + fieldError.getDefaultMessage());
                    } else {
                        return CommonResult.dataBindingFailed();
                    }
                }
            }
        }
        return joinPoint.proceed();
    }
}
