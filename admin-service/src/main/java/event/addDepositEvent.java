package event;

public class addDepositEvent extends withdrawEvent {

    public static int direction  = -1; //扣钱
    public static int is_addbill = 1;
    public addDepositEvent(Object source) {
        super(source);
    }
}
