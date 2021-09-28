package com.mapper.frontend;

import com.stock.models.MemberHeYueApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberHeYueApplyMapper {
    int addOneHeYueApply(Map map);
    List findMemberHeYueApplyListByPage(Map map);

    int updateHeYueApplyState(Map map);
    List selectMemberHeYueByCase(Map map);
    MemberHeYueApply selectHeyueById(Integer id);
    int addDeposit(Map map);
    int decreaseHeYueMoney(Map map);
    int updateHeYueStartTime(Map map);
    int updateHeYueEndTime(Map map);
    List selectMemberHeYueByStates();
    int increaseHeYueMoney(Map map);
    int decreaseHeYueProfit(Map map);
    int increaseHeYueProfit(Map map);

    int expandHeYue(Map map);

    List selectHistoryHeYue(Map<Object, Object> map);


    List findCanEffectHeYue();

    List getHeYueProfitRankList();

    int addMemberInterest(Map data);

    double findMemberInterestByCase(Map<String, Object> map);
    Integer testss();

    void updateHeYueCloseDate(Integer member_heyue_id);

    void addForceLog(Map force);

    void addHeYue(Map map);

    void updateMemberHeYue(Map map);
}
