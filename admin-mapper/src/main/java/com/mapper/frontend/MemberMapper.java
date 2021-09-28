package com.mapper.frontend;


import com.stock.models.form.registerForm;
import com.stock.models.frontend.Member;
import com.stock.models.frontend.UserBank;
import com.stock.models.frontend.UserBill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {
    Member findMember(String username);
    Member findMemberById(Integer id);
    int decreaseMemberAmount(Map map);
    int increaseMemberAmount(Map map);
    List findMemberHeYueApplyListByPage(Map map);
    int updatePassword(Map map);
    String findpassword(Integer id);
    UserBank findUserBank(Integer uid);
    Boolean updateUserBank(UserBank userBank);
    Boolean addUserBank(UserBank userBank);
    int applyWithdraw(Map map);
    Boolean addBill(Map map);

    Integer findMemberByInviteCode(String invite_code);

    int register(registerForm data);

    List getFundListByPage(Map map);

    Map getMemberInfo(int id);

    List getMemberListByPage(Map map);

    int updateMemberState(Map map);

    Member findMemberByPhone(Object phone);

    void updateMemberPassword(Map map);

    void updateUserNickName(UserBank userBank);

    List findMemberInterestListByPage(Map<String, Object> map);

    double findMemberSumInterest(Map<String, Object> map);

    List<UserBill> getfinaceListByPage(Map map);

    void createMember(Map map);

    void addMemberFengKong(Map map);

    Map findMemberFengKong(int id);
}
