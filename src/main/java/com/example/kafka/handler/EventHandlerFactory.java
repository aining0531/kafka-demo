package com.example.kafka.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EventHandlerFactory {

    private final Map<String, EventHandler<?>> handlerMap;
    private final ObjectMapper objectMapper;

    // Spring 自动注入所有实现 EventHandler 接口的 Bean
    public EventHandlerFactory(List<EventHandler<?>> handlers, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        EventHandler::getEventType,
                        h -> h,
                        (v1, v2) -> v1 // 如果有重复，取第一个
                ));
        log.info("✅ 注册了 {} 个事件处理器", handlerMap.size());
    }

    // 根据类型获取处理器
    public EventHandler<?> getHandler(String eventType) {
        return Optional.ofNullable(handlerMap.get(eventType))
                .orElseThrow(() -> new IllegalArgumentException("未知事件类型：" + eventType));
    }

    // 反序列化并执行
    @SuppressWarnings("unchecked")
    public void dispatch(String eventType, String jsonPayload) {
        EventHandler<Object> handler = (EventHandler<Object>) getHandler(eventType);
        try {
            // 根据处理器定义的类型反序列化 JSON
            Object payload = objectMapper.readValue(jsonPayload, handler.getPayloadType());
            handler.handle(payload);
        } catch (JsonProcessingException e) {
            log.error("JSON 反序列化失败：{}", jsonPayload, e);
            throw new RuntimeException(e);
        }
    }
}
