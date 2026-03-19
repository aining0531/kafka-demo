package com.example.kafka.controller;

import com.example.kafka.model.OrderEvent;
import com.example.kafka.model.PaymentEvent;
import com.example.kafka.producer.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventProducer producer;

    @PostMapping("/order")
    public String sendOrder(@RequestBody OrderEvent event) throws Exception {
        producer.sendOrderEvent(event);
        return "Order Sent";
    }

    @PostMapping("/payment")
    public String sendPayment(@RequestBody PaymentEvent event) throws Exception {
        producer.sendPaymentEvent(event);
        return "Payment Sent";
    }
}