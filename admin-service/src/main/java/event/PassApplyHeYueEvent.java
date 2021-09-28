package event;

public class PassApplyHeYueEvent extends passWithdrawEvent {
    public static int direction  = 0; //加钱
    public static int is_addbill = 0;//加单
    public PassApplyHeYueEvent(Object source) {
        super(source);
    }
}
