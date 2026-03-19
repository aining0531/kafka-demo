package com.example.kafka.handler;

// 1. 处理器接口
public interface EventHandler<T> {
    // 支持的事件类型（对应 Header 中的值）
    String getEventType();
    // 负载的目标类（用于反序列化）
    Class<T> getPayloadType();
    // 处理逻辑
    void handle(T payload);
}

