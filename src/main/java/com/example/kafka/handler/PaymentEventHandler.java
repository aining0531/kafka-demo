package com.example.kafka.handler;

import com.example.kafka.model.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentEventHandler implements EventHandler<PaymentEvent> {
    @Override
    public String getEventType() { return "PAYMENT_COMPLETED"; }

    @Override
    public Class<PaymentEvent> getPayloadType() { return PaymentEvent.class; }

    @Override
    public void handle(PaymentEvent payload) {
        log.info("💰 处理支付：{}, 状态：{}", payload.getPaymentId(), payload.getStatus());
        // 业务逻辑...
    }
}
