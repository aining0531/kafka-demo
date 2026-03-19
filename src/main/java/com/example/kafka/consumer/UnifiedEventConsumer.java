package com.example.kafka.consumer;

import com.example.kafka.handler.EventHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class UnifiedEventConsumer {

    @Autowired
    private EventHandlerFactory handlerFactory;

    @KafkaListener(topics = "biz-events-topic", groupId = "biz-event-group")
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String eventType = null;
        try {
            // 1. 从 Header 获取事件类型
            Header header = record.headers().lastHeader("event-type");
            if (header == null) {
                log.error("❌ 缺少 event-type 头，Offset: {}", record.offset());
                ack.acknowledge(); // 丢弃无效消息
                return;
            }
            eventType = new String(header.value(), StandardCharsets.UTF_8);

            // 2. 路由并处理
            log.info("📩 收到事件：{}, Key: {}, Offset: {}", eventType, record.key(), record.offset());
            handlerFactory.dispatch(eventType, record.value());

            // 3. 手动提交偏移量
            ack.acknowledge();

        } catch (Exception e) {
            log.error("⚠️ 事件处理失败：{}, Offset: {}", eventType, record.offset(), e);
            // 策略选择：
            // 1. 抛出异常触发 Spring Retry (配置重试后进入 DLQ)
            throw e;
            // 2. 或者 ack.acknowledge() 直接丢弃 (不推荐)
        }
    }
}