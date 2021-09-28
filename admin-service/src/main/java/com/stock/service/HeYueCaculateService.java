package com.stock.service;

public interface HeYueCaculateService {

    //总金额
    double cal_total_capitial(double deposit, double leverage_money);

    //保证金
    double cal_deposit(double deposit);

    //配资金额
    double cal_leverage_money(double deposit, int leverage_id);

    //止损预警线
    double cal_loss_warning_line(double deposit, double loss_warning_rate, double leverage_money);

    //止损平仓线
    double cal_loss_sell_line(double deposit, double loss_sell_rate, double leverage_money);

    //利率
    double cal_interest_rate(int heyue_id, int leverage_id);

    //利息
    double cal_interest(double rate,double leverage_money, int base_num);

    //资金使用时间
    int cal_capitial_used_time(int heyue_id);

    //准备资金
    double cal_repare_capitical(double deposit, double interest);
}
