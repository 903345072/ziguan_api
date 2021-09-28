package com.stock.service;

import com.stock.models.User;
import com.stock.models.form.registerForm;
import com.stock.models.frontend.Member;
import com.stock.models.frontend.UserBank;
import com.stock.models.frontend.UserBill;

import java.util.List;
import java.util.Map;

public interface MemberService {
    List findMemberHeYueApplyList(Map map);

    int updateHeYueApplyState(Map map);
    List selectMemberHeYueByCase(Map map);
    int updatePassword(Map map);
    String findpassword(Integer id);
    UserBank findUserBank(Integer uid);
    Boolean updateUserBank(UserBank userBank, UserBank userBank_);
    Boolean applyWithdraw(Map map);
    Boolean addBill(Map map);
    Member findMemberById(Integer id);
    int decreaseMemberAmount(Map map);
    int increaseMemberAmount(Map map);
    void renewHeYue(Integer member_heyue_id,Integer flag);
    List selectHistoryHeYue(Map<Object, Object> map);


    Integer findMemberByInviteCode(String invite_code);

    int register(registerForm data);

    List getFundListByPage(Map map);

    Map getMemberInfo(int id);

    double findMemberInterestByCase(Map<String, Object> map);

    List getMemberList(Map map);

    int updateMemberState(Map map);


    Member findMemberByPhone(Object phone);

    void updateMemberPassword(Map map);
    void xuyue(Integer member_heyue_id);


    void updateUserNickName(UserBank userBank);

    List findMemberInterestList(Map<String, Object> map);

    double findMemberSumInterest(Map<String, Object> map);

    List<UserBill> getFinaceListByPage(Map map);

    void createMember(Map map);

    void addMemberFengKong(Map map);

    Map findMemberFengKong(int id);
}
