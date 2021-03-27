package com.gecko.ribbitmq.direct;


import com.gecko.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 *  DirectExchange消息接收类
 * 	注意：
 * 	1、使用Exchange的direct模式时接收者的RoutingKey必须要与发送时的RoutingKey完全一致否则无法获取消息
 * 	2、接收消息时队列名也必须要发送消息时的完全一致
 */
public class DirectMessageReceiver {
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtil.getChannel();
        // 声明关系 可省，提供者已完成声明
        // 监听队列
        channel.basicConsume("directQueue", true, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("DirectExchangeMsg: " + new String(body));
            }
        });
    }
}
