package com.mapper;



import com.stock.models.HeYue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@Mapper
public interface HeYueMapper {
    List findHeYueByPage(Map map);
    Map findHeYueNameById(@Param(value = "id") Integer id);
    List<Map> findValidHeYueIdList();
    int findHeYueCount(Map map);
    int insertOneHeYue(com.stock.models.HeYue heYu);
    int updateOneHeYue(com.stock.models.HeYue heY);
    int updateHeYueStaus(Map map);
}
