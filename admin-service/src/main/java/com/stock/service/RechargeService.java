package com.stock.service;

import com.stock.models.Recharge;
import com.stock.models.form.rechargeForm;


import java.util.List;
import java.util.Map;

public interface RechargeService {
    List<?> getRechargeList(Map<String,Object> map);
    int updateRechargeState(Map<String,Object> map);
    Recharge findOneRecharge(Integer id);

    int dorecharge(Recharge data);
}
