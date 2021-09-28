package com.util;

public enum BillCode {
    RECHARGE(1),//充值
    RECHARGE_BACK(2),//充值返现
    APPLY_WITHDRAW(3),//申请提现
    WIDRAW_FAILD(4),//提现失败
    RECHARGE_BY_MAN(5),//人工转入
    ROLLOUT_BY_MAN(6),//人工转出
    APPLY_HEYUE(7),//申请合约
    EXPAND_HEYUE(8),//扩大合约
    ADD_TO_DEPOSIT(9),//追加保证金
    HEYUE_PROFIT(10),//合约提盈
    HEYUE_BALANCE(11),//合约结算
    EXTENSION_FEE(12),//续期利息
    BACK_MONEY(13),//下线返佣
    HEYUE_APPLY_FAILD(14),//合约申请失败
    DO_ORDER(15),//下单修改合约资金
    SELL_STOCK(16),//卖出股票
    CANCLE_ORDER(17),//撤销股票订单
    PART_TRADE(18),
    HEYUE_ADD_PROFIT(19),//买股增加合约盈利
    STOCK_TRADE_DIFF(20),//买股增补充差价
    STOCK_BUY_FEE(21),
    ADD_TO_DEPOSIT_HEYUE(22);
    private int code;
    private BillCode(Integer code) {
        this.code = code;
    }
    public Integer getCode() {
        return this.code;
    }
    public void setCode(int code) {
        this.code = code;
    }
}
