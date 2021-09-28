package listener;


import com.stock.service.MemberHeYueApply;
import com.stock.service.OrderService;
import event.SellOrderEvent;
import event.makeOrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(1)
@Component
public class makeOrderListener4 implements ApplicationListener<SellOrderEvent> {

    @Autowired
    MemberHeYueApply MemberHeYueApplyImpl;
    @Autowired
    OrderService orderServiceImpl;
    //扣手数，并冻结手数
    @Override
    public void onApplicationEvent(SellOrderEvent makeOrderEvent) {
        Map source = (Map)makeOrderEvent.getSource();
        orderServiceImpl.freezeHand(source);

    }
}
