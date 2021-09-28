package listener;


import com.stock.service.MemberHeYueApply;
import event.makeOrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(1)
@Component
public class makeOrderListener1 implements ApplicationListener<makeOrderEvent> {

    @Autowired
    MemberHeYueApply MemberHeYueApplyImpl;
    //扣合约的钱
    @Override
    public void onApplicationEvent(makeOrderEvent makeOrderEvent) {
        Map source = (Map)makeOrderEvent.getSource();

            MemberHeYueApplyImpl.decreaseHeYueMoney(source);


    }
}
