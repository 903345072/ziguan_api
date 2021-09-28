package com.mapper;

import com.stock.models.User;
import com.stock.models.Withdraw;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WithdrawMapper {
    List<?> findWithdrawListByPage(Map map);
    int updateWithdrawState(Map map);
    Withdraw findOneWithdraw(Integer id);
}
