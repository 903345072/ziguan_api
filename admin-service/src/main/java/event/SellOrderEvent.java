package event;

import org.springframework.context.ApplicationEvent;

public class SellOrderEvent extends ApplicationEvent{

    //扣合约的钱
    //增加账单记录
    //返利
    public SellOrderEvent(Object source) {
        super(source);
    }
}
