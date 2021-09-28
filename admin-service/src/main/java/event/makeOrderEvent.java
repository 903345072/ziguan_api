package event;

import org.springframework.context.ApplicationEvent;

public class makeOrderEvent  extends ApplicationEvent{

    //扣合约的钱
    //增加账单记录
    //返利
    public makeOrderEvent(Object source) {
        super(source);
    }
}
