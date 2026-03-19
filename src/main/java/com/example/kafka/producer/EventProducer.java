package com.example.kafka.producer;

import com.example.kafka.model.OrderEvent;
import com.example.kafka.model.PaymentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class EventProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendOrderEvent(OrderEvent event) throws JsonProcessingException {
        String topic = "biz-events-topic";
        String key = event.getOrderId();
        String value = objectMapper.writeValueAsString(event);

        // 构建消息头
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        record.headers().add("event-type", "ORDER_CREATED".getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);
        log.info("🚀 发送订单事件：{}", event.getOrderId());
    }

    public void sendPaymentEvent(PaymentEvent event) throws JsonProcessingException {
        String topic = "biz-events-topic";
        String key = event.getPaymentId();
        String value = objectMapper.writeValueAsString(event);

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        record.headers().add("event-type", "PAYMENT_COMPLETED".getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);
        log.info("🚀 发送支付事件：{}", event.getPaymentId());
    }
}
