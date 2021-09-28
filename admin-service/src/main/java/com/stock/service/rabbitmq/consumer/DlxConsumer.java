package com.stock.service.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.stock.service.rabbitmq.productor.OrderProductor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DlxConsumer {
    @Autowired
    private OrderProductor produce;
    @RabbitListener(queues = "queue_dlx",autoStartup = "false")
    public void process(String hello, Channel channel, Message msg) throws IOException {
        System.out.println(213);
        channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);


    }
}
