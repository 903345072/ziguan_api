package com.stock.service;

import java.util.List;
import java.util.Map;

public interface MemberHeYueApply {

    /**
     * 申请一个新的合约
     *
     * @param map
     * @return
     */

    int addOneHeYueApply(Map map);

    /**
     * 追加保证金
     * @param map
     * @return
     */
    int addDeposit(com.stock.models.form.addDeposit map) throws Exception;

    /**
     * 查找一个用户合约
     * @param id
     * @return
     */
    com.stock.models.MemberHeYueApply selectHeyueById(Integer id);

    /**
     * 更新合约余额
     * @param map
     * @return
     */
    int decreaseHeYueMoney(Map map);
    int updateHeYueStartTime(Map map);
    int updateHeYueApplyState(Map map);
    int updateHeYueEndTime(Map map);
    List selectMemberHeYueByStates();
    int increaseHeYueMoney(Map map);
    int decreaseHeYueProfit(Map map);
    int increaseHeYueProfit(Map map);
    int closeHeYue(Map map);

    int pickHeYueProfit(Map map);

    int expandHeYue(Map map);

    List findCanEffectHeYue();

    List getHeYueProfitRankList();


    int addMemberInterest(Map data);

    double findMemberInterestByCase(Map<String, Object> map);

    void addForceLog(Map force);

    void addHeYue(Map map);

    void updateMemberHeYue(Map map);
}
