package com.gecko.ribbitmq.fanout;


import com.gecko.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * 	注意：
 * 	1、使用fanout模式获取消息时不需要绑定特定的队列名称，只需使用channel.queueDeclare().getQueue();
 * 	    获取一个随机的队列名称，然后绑定到指定的Exchange即可获取消息。
 * 	2、这种模式中可以同时启动多个接收者只要都绑定到同一个Exchange即可让所有接收者同时接收同一个消息是一种广播的消息机制
 */
public class FanoutMessageReceiver {
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtil.getChannel();
        // 声明随机消息队列名称 默认在没有使用的情况下，会将随机队列删除
        String randomQueueName = channel.queueDeclare().getQueue();
        // 声明绑定
        channel.queueBind(randomQueueName, "fanoutExchange", "");
        // 接收消息，消费消息
        channel.basicConsume(randomQueueName, true, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("ReceiveFanoutExchangeMsg: > " + new String(body));
            }
        });

    }
}
