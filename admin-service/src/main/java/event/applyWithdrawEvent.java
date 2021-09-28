package event;

import org.springframework.context.ApplicationEvent;

public  class applyWithdrawEvent extends withdrawEvent {

    public static int direction  = -1; //扣钱
    public static int is_addbill = 1;
    public applyWithdrawEvent(Object source) {
        super(source);
    }
}
