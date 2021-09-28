package event;

public  class passRechargeEvent extends withdrawEvent {
    public static int direction  = 1; //加钱
    public static int is_addbill = 1;
    public passRechargeEvent(Object source) {
        super(source);
    }
}
