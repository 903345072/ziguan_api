package com.mapper;



import com.stock.models.Leverage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeverageMapper {
    List findLeverageByPage(Map map);
    List<Map> findValidLeverageIdList();
    Map findLeverageNameById(@Param(value = "id") Integer id);
    int findLeverageCount(Map map);
    int insertOneLeverage(com.stock.models.Leverage leverage);
    int updateOneLeverage(com.stock.models.Leverage leverage);
    int updateLeverageStaus(Map map);

}
