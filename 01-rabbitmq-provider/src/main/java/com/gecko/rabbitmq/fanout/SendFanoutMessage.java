package com.gecko.rabbitmq.fanout;


import com.gecko.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * 	fanout模式的消息发送需要将一个消息同时绑定到多个队列中因此这里不能创建并指定某个队列
 * 	fanout模式不需要制定routingKey
 * 	channel.queueDeclare().getQueue;  无参方法,声明的是随机名称的消息队列 内部返回的是
 *        public AMQP.Queue.DeclareOk queueDeclare() throws IOException {
 *            return queueDeclare("", false, true, true, null);
 *        }
 *  如果使用的是随机名称的消息队列，需要在消费者方声明关系
 *  若在生产发送方声明了随机消息队列名称，消费者方是无法确定并接收到消息的
 *  想接收，在控制台查看消息队列的名称进行制定也可以
 */
public class SendFanoutMessage {
    public static void main(String[] args) throws IOException {
        String message = "this's fanoutExchange Message";

        Channel channel = RabbitMQUtil.getChannel();
        // 声明交换机
        channel.exchangeDeclare("fanoutExchange", "fanout", true);
        // 发送消息
        channel.basicPublish("fanoutExchange", "", null, message.getBytes());
        RabbitMQUtil.releaseRabbitMQ();
        System.out.println("FanoutExchange Message Send Successful!");
    }
}
