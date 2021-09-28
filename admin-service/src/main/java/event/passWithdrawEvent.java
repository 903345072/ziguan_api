package event;

public  class passWithdrawEvent extends withdrawEvent {
    public static int direction  = 0; //扣钱
    public static int is_addbill = 0;
    public passWithdrawEvent(Object source) {
        super(source);
    }
}
