package com.stock.service;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface InterestService {
    List findInterest();
    int setInterest(List list);
    int deleteAllInterest();
    Map findInterestByIds(int heyue_id, int leverage_id);
}


