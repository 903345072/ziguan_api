package com.stock.service;

import com.stock.models.HeYue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HeYueService {
    List findHeYue(Map map);
    int findHeYueCount(Map map);
    int insertOneHeYue(com.stock.models.HeYue heYu);
    int updateOneHeYue(com.stock.models.HeYue heY);
    int updateHeYueStaus(Map map);
    Map findHeYueNameById(Integer id);
    List<Map> findValidHeYueIdList();
}


