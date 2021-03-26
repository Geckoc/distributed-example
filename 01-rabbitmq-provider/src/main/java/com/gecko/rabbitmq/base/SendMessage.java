package com.gecko.rabbitmq.base;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *  普通发送，没有将消息发送到交换机中，直接发送到队列
 */
public class SendMessage {
    public static void main(String[] args) {
        // 创建连接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置RabbitMQ的主机IP
        connectionFactory.setHost("192.168.182.131");
        // 设置RabbitMQ的端口号
        connectionFactory.setPort(5672);
        // 设置RabbitMQ的用户名
        connectionFactory.setUsername("root");
        // 设置RabbitMQ的密码
        connectionFactory.setPassword("root");
        // 设置虚拟主机路径
        connectionFactory.setVirtualHost("/");
        // 定义连接对象
        Connection connection = null;
        // 定义管道对象
        Channel channel = null;
        try {
            // 通过连接工厂实例化连接对象
            connection = connectionFactory.newConnection();
            // 通过连接对象实例化管道对象
            channel = connection.createChannel();
            /* channel可以创建消息队列、创建交换机、发送消息、监听消息、接收消息等等...
            queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,
                         Map<String, Object> arguments) throws IOException
             参数1：队列名称，参数2：是否持久化，参数3：是否排外，参数4：是否自动删除，参数5：消息队列的参数设置
             */
            channel.queueDeclare("baseQueue", true, false, false, null);
            // 消息内容
            String message = "this's my first Message";
            /*
                发送消息
                void basicPublish(String exchange, String routingKey,
                                BasicProperties props, byte[] body) throws IOException;
                参数1：交换机名称，参数2：路由键，参数3：基本属性，参数4：消息的二进制数组
                目前发送的是普通消息，直达消息队列，没有交换机
                没有交换机则路由键设置为消息队列的名称
             */
            channel.basicPublish("", "baseQueue", null, message.getBytes());
            System.out.println("Msg Send Success :::> " + message);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭资源
                if (channel != null) {
                    channel.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }

        }
    }
}
