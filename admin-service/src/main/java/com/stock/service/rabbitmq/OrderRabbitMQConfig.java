package com.stock.service.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Component
public class OrderRabbitMQConfig {
    public static  String HEYUE_DELAY_EFFCET_QUEUE = "heyue_delay_effect_queque";

    public static  String ORDER_QUEUE = "order_queque";
    public static  String ORDER_EXCHANGE_NAME = "order_exchange_name";

    public static  String HEYUE_DELAY_EFFECT_EXCHANGE_NAME = "heyue_delay_effect_exchange";
    public static  String ORDER_ROUTE_KEY = "orderRoutingKey";

    public static  String HEYUE_DELAY_EFFECT_ROUTE_KEY = "heyueDelayEffectRoutingKey";




    @Bean
    public Queue heyue_delay_effect_que() {
        return new Queue(HEYUE_DELAY_EFFCET_QUEUE,true,true,true);
    }
    //定义延迟交换机
    @Bean
    DirectExchange heyueDelayExchange(){
        DirectExchange s =   new DirectExchange(HEYUE_DELAY_EFFECT_EXCHANGE_NAME);
        s.setDelayed(true);
        return s;
    }
    //定义延迟合约延迟生效绑定
    @Bean
    public Binding heyueDelayPayBind() {
        return BindingBuilder.bind(heyue_delay_effect_que()).to(heyueDelayExchange()).with(HEYUE_DELAY_EFFECT_ROUTE_KEY);
    }



    //定义订单队列
    @Bean
    public Queue directOrderDicQueue() {
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","exchange-dlx");
        arguments.put("x-dead-letter-routing-key","*");

        return new Queue(ORDER_QUEUE,true,true,true,arguments);
    }

    //定义普通交换机交换机
    @Bean
    DirectExchange directOrderExchange() {
        return new DirectExchange(ORDER_EXCHANGE_NAME);
    }



    //绑定队列与交换机
    @Bean
    Binding bindingExchangeOrderDicQueue() {
        return BindingBuilder.bind(directOrderDicQueue()).to(directOrderExchange()).with(ORDER_ROUTE_KEY);
    }
}
