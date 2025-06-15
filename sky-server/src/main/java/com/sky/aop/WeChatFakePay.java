package com.sky.aop;

import com.sky.dto.OrdersPaymentDTO;

import com.sky.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class WeChatFakePay {

    @Autowired
    private OrdersService ordersService;

    @Around("@annotation(com.sky.anno.WeChatFakePay)")
    public Object WeChatFakePayAspect(ProceedingJoinPoint joinPoint){
        Object arg = joinPoint.getArgs()[0];

        if (!(arg instanceof OrdersPaymentDTO)) {
            return null;
        }

        OrdersPaymentDTO ordersPaymentDTO = (OrdersPaymentDTO) arg;

        ordersService.paySuccess(ordersPaymentDTO.getOrderNumber());

        return null;
    }
}
