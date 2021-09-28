package com.stock.service.rabbitmq.productor;

import com.stock.service.rabbitmq.OrderRabbitMQConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderProductor {
    @Autowired
    private AmqpTemplate rabbitTemplate;


    public void send_heyue_delay(Integer member_heyue_id,int expire){
        this.rabbitTemplate.convertAndSend(OrderRabbitMQConfig.HEYUE_DELAY_EFFECT_EXCHANGE_NAME,OrderRabbitMQConfig.HEYUE_DELAY_EFFECT_ROUTE_KEY,member_heyue_id, message -> {
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setDelay(expire);   // 毫秒为单位，指定此消息的延时时长
            return message;});
    }
}
