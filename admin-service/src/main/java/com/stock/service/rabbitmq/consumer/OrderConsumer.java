package com.stock.service.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import com.stock.models.frontend.Member;
import com.stock.service.InterestService;
import com.stock.service.MemberHeYueApply;
import com.stock.service.MemberService;
import com.stock.service.ServiceImpl.HeYueCaculateAdpterInterface;
import com.stock.service.ServiceImpl.MemberServiceImpl;
import com.stock.service.rabbitmq.OrderRabbitMQConfig;
import com.stock.service.rabbitmq.productor.OrderProductor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class OrderConsumer {

    enum Action{
        ACCEPT, // 处理成功
        RETRY, // 可以重试的错误
        REJECT, // 无需重试的错误
    }
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MemberHeYueApply MemberHeYueApplyImpl;
    @Autowired
    MemberService MemberServiceImpl;

    @Autowired
    com.stock.service.ServiceImpl.MemberServiceImpl m;
    @Autowired
    @Qualifier("HeYueCaculateAdper")
    HeYueCaculateAdpterInterface HeYueCaculateImpl;

    public static boolean isWeekend() throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }
    @RabbitListener(queues = "heyue_delay_effect_queque")
    public void consume2(Integer member_heyue_id, Channel channel, Message message) throws IOException, ParseException {

        Action action=Action.ACCEPT;
        long tag=message.getMessageProperties().getDeliveryTag();
        try {
            //进行合约续期处理....
            //todo
            if (!isWeekend()){
                m.renewHeYue(member_heyue_id,1);//扣钱/改状态
            }
        } catch (Exception e) {
            e.printStackTrace();
            String message1 = e.getMessage();
            action = Action.RETRY;
            throw e;
        }finally {
            try {
                // 通过finally块来保证Ack/Nack会且只会执行一次
                if (action == Action.ACCEPT) {
                    channel.basicAck(tag,true);
                    m.xuyue(member_heyue_id);
                    // 拒绝消息也相当于主动删除mq队列的消息
                }else {
                    channel.basicNack(tag, false, true);
                    Thread.sleep(2000L);
                    // 拒绝消息也相当于主动删除mq队列的消息
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @RabbitListener(queues = "order_queque")
    public void order_consum(Integer order_id, Channel channel, Message message){

    }
}
