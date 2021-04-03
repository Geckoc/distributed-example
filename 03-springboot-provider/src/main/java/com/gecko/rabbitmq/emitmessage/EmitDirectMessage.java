package com.gecko.rabbitmq.emitmessage;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class EmitDirectMessage {
    @Resource
    private AmqpTemplate amqpTemplate;
    // 消息发送前需声明关系 RabbitMQConfig配置类中

    public void emit(){
        String message = "hello SpringBoot RabbitMQ";
        amqpTemplate.convertAndSend("directExchange" ,"directRoutingKey", message);
    }
}
