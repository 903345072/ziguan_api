package listener;

import event.makeOrderEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(3)
@Component
public class makeOrderListener3 implements ApplicationListener<makeOrderEvent> {
    @Override
    public void onApplicationEvent(makeOrderEvent makeOrderEvent) {
        Map source = (Map)makeOrderEvent.getSource();
        //把此订单放入队列,实时更新redis订单数据，并判断是否达到预警线，止损线

    }
}
