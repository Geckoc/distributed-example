package com.gecko.rabbitmq.direct;

import com.gecko.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * Exchange交换机  direct模式发送消息
 *  1、使用Exchange的direct模式时接收者的RoutingKey必须要与发送时的RoutingKey完全一致否则无法获取消息
 * 	2、接收消息时队列名也必须要发送消息时的完全一致
 */
public class SendDirectMessage {
    public static void main(String[] args) {
        try {
            // 获取实例管道对象
            Channel channel = RabbitMQUtil.getChannel();
            /*
               声明定义交换机
                exchangeDeclare(String exchange, String type, boolean durable)
                参数1：交换机名称，参数2：交换机类型，参数3：是否持久化
                exchange：交换机类型取值为 direct、queue、topic、headers
             */
            channel.exchangeDeclare("directExchange","direct", true);
            /*
               声明队列
                queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,
                                 Map<String, Object> arguments)
                参数1：队列名称，参数2：是否持久化，参数3：是否排外，参数4：是否自动删除，参数5：消息队列的参数设置
                exclusive:排外,同一时间内只能有一个消费者消费消息，false=不排外
             */
            channel.queueDeclare("directQueue", true, false, false, null);
            /*
              声明关系: 绑定消息队列和交换机之间的关联
              Binding
                用于消息队列和交换器之间的关联。一个绑定就是基于路由键将交换器和消息队列连接起来的路由规则
                queueBind(String queue, String exchange, String routingKey)
                参数1：消息队列名称，参数2：交换机名称，参数3：路由键
             */
            channel.queueBind("directQueue", "directExchange", "directRoutingKey");
            // 消息内容
            String message = "this's directExchange message";
            // 发送消息到交换机中
            channel.basicPublish("directExchange", "directRoutingKey", null, message.getBytes());
            System.out.println("directExchangeMode Send Message Successful! ");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            RabbitMQUtil.releaseRabbitMQ();
        }
    }
}
