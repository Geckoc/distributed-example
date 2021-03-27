package com.gecko.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class RabbitMQUtil {
    // 定义连接工厂
    private static ConnectionFactory connectionFactory;
    // 定义连接对象
    private static Connection connection = null;
    // 定义管道对象
    private static Channel channel = null;
    // 连接资源类加载时只需设置一次
    static {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.182.131");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");
        connectionFactory.setVirtualHost("/");
    }
    /**
     * 获取管道对象公共方法
     */
    public static Channel getChannel(){
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return channel;
    }

    /**
     * 关闭资源
     */
    public static void releaseRabbitMQ(){
        try {
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
