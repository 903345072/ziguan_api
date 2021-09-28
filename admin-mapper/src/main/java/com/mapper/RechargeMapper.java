package com.mapper;

import com.stock.models.Recharge;
import com.stock.models.form.rechargeForm;


import java.util.List;
import java.util.Map;

public interface RechargeMapper {
    List<?> findRechargeListByPage(Map map);
    int updateRechargeState(Map map);
    Recharge findOneRecharge(Integer id);

    int dorecharge(Recharge data);
}
