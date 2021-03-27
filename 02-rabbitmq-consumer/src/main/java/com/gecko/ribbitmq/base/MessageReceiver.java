package com.gecko.ribbitmq.base;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessageReceiver {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.182.131");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("root");
        factory.setVirtualHost("/");
        // 定义连接对象
        Connection connection = null;
        // 定义管道对象
        Channel channel = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            /*
              细节：如果消息发送者[生产者]已经声明了消息队列或交换机
              在消费者中则可不再次声明，生产者和消费者必须有一方声明消费队列或交换机
              注：重复声明不会报错，双方都未声明则报错
              String basicConsume(String queue, boolean autoAck, Consumer callback)
              参数1：消息队列名称，参数2：是否自动应答，参数3：回调方法，在该方法中获取消息
              autoAck：true 代表当前的消费者自动消费这条消息，消息队列自动移除
                       false 手动应答，需再代码中进行手动确认操作，这样消息才会从消息队列移除
             */
            channel.queueDeclare("baseQueue", true, false, false, null);
            // 监听队列
            channel.basicConsume("baseQueue", true, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body);
                    System.out.println("MessageContent >>> " + message);
                }
            });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源 消费者如果关闭了资源，则一次只能消费一条消息
           /* try {
                if (channel != null) {
                    channel.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }*/
        }

    }
}
