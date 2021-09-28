package com.stock.service;

import com.stock.models.HeYue;
import com.stock.models.Leverage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LeverageService {
    List findLeverage(Map map);
    int findLeverageCount(Map map);
    int insertOneLeverage(Leverage leverage);
    int updateOneLeverage(Leverage leverage);
    int updateLeverageStaus(Map map);
    Map findLeverageNameById(Integer id);
    List<Map> findValidLeverageIdList();
}


