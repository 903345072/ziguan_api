package com.stock.service;

import com.stock.models.Withdraw;

import java.util.List;
import java.util.Map;

public interface WithdrawService {
    List<?> getWithdrawList(Map<String,Object> map);
    int updateWithdrawState(Map<String,Object> map);
    Withdraw findOneWithdraw(Integer id);
}
