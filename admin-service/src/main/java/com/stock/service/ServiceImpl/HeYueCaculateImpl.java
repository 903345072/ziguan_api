package com.stock.service.ServiceImpl;

import com.stock.service.HeYueCaculateService;
import com.stock.service.HeYueService;
import com.stock.service.InterestService;
import com.stock.service.LeverageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.NumberFormat;
import java.util.HashMap;

@Service
public class HeYueCaculateImpl implements HeYueCaculateService {

    @Autowired
    LeverageService LeverageImpl;

    @Autowired
    InterestService InterestImpl;

    @Autowired
    HeYueService HeYueImpl;
    /**
     *
     * @param deposit 保证金
     * @param leverage_money 配资资金
     * @return 总资金
     */
    @Override
    public double cal_total_capitial(double deposit, double leverage_money) {
        return deposit+leverage_money;
    }

    @Override
    public double cal_deposit(double deposit) {
        return deposit;
    }


    @Override
    public double cal_leverage_money(double deposit, int leverage_id) {
        HashMap map = (HashMap) LeverageImpl.findLeverageNameById(leverage_id);
        return deposit*(double)map.get("name") ;
    }


    @Override
    public double cal_loss_warning_line(double deposit, double loss_warning_rate, double leverage_money) {
        return deposit*loss_warning_rate+leverage_money;
    }

    @Override
    public double cal_loss_sell_line(double deposit, double loss_sell_rate, double leverage_money) {
        return deposit*loss_sell_rate+leverage_money;
    }

    @Override
    public double cal_interest_rate(int heyue_id, int leverage_id) {
        HashMap map = (HashMap) InterestImpl.findInterestByIds(heyue_id,leverage_id);
        return (double)map.get("rate");
    }

    @Override
    public double cal_interest(double interest_rate, double leverage_money,int heyue_base_num) {
        return interest_rate*leverage_money*heyue_base_num/100;
    }

    @Override
    public int cal_capitial_used_time(int heyue_id) {
        HashMap map = (HashMap) HeYueImpl.findHeYueNameById(heyue_id);
        return (Integer) map.get("base_num");
    }

    @Override
    public double cal_repare_capitical(double deposit, double interest) {
        return deposit+interest;
    }
}
