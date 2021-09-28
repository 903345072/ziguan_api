package event;

public  class refuseRechargeEvent extends withdrawEvent {
    public static int direction  = 0; //加钱
    public static int is_addbill = 0;
    public refuseRechargeEvent(Object source) {
        super(source);
    }
}
