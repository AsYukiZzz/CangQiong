package com.sky.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderPaidEvent extends ApplicationEvent {
    private final String orderNumber;

    public OrderPaidEvent(Object source, String orderNumber) {
        super(source);
        this.orderNumber = orderNumber;
    }
}
