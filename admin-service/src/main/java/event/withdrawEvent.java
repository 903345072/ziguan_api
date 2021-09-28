package event;

import org.springframework.context.ApplicationEvent;

public  class withdrawEvent extends ApplicationEvent {
    public static int direction;
    public static int is_addbill;
    public withdrawEvent(Object source) {
        super(source);
    }
}
