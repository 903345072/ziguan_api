package event;

public class RefuseApplyHeYueEvent extends RefuseWithdrawEvent {
    public static int direction  = 1; //加钱
    public static int is_addbill = 1;//加单
    public RefuseApplyHeYueEvent(Object source) {
        super(source);
    }
}
