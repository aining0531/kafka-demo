package com.example.kafka.handler;

import com.example.kafka.model.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent> {
    @Override
    public String getEventType() { return "ORDER_CREATED"; }

    @Override
    public Class<OrderEvent> getPayloadType() { return OrderEvent.class; }

    @Override
    public void handle(OrderEvent payload) {
        log.info("📦 处理订单：{}, 金额：{}", payload.getOrderId(), payload.getAmount());
        // 业务逻辑...
    }
}
