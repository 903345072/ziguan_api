package event;

import org.springframework.context.ApplicationEvent;

public class RefuseWithdrawEvent extends withdrawEvent {
    public static int direction  = 1; //加钱
    public static int is_addbill = 1;
    public RefuseWithdrawEvent(Object source) {
        super(source);
    }
}
