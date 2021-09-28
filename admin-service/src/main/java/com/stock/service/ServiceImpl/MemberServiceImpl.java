package com.stock.service.ServiceImpl;


import com.mapper.frontend.MemberHeYueApplyMapper;
import com.mapper.frontend.MemberMapper;
import com.stock.models.MemberHeYueApply;
import com.stock.models.form.registerForm;
import com.stock.models.frontend.Member;
import com.stock.models.frontend.UserBank;
import com.stock.models.frontend.UserBill;
import com.stock.service.MemberService;
import com.stock.service.RechargeService;
import com.stock.service.rabbitmq.productor.OrderProductor;
import com.util.BillCode;
import com.util.Holiday;
import event.PassApplyHeYueEvent;
import event.RefuseApplyHeYueEvent;
import event.applyWithdrawEvent;
import event.withdrawEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MemberServiceImpl implements MemberService {
    @Value("${widraw_fee}")     private double widraw_fee;
    @Autowired
    MemberHeYueApplyMapper memberHeYueApplyMapper;

    @Autowired
    com.stock.service.MemberHeYueApply MemberHeYueApplyImpl;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    OrderProductor orderProductor;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;



    @Autowired
    @Qualifier("HeYueCaculateAdper")
    HeYueCaculateAdpterInterface HeYueCaculateImpl;

    @PostConstruct
    public void init(){

    }
    @Override
    public List findMemberHeYueApplyList(Map map) {
        return memberHeYueApplyMapper.findMemberHeYueApplyListByPage(map);
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateHeYueApplyState(Map map) {

        int ret = memberHeYueApplyMapper.updateHeYueApplyState(map);
        //增加账单，加钱
        MemberHeYueApply member_hy = memberHeYueApplyMapper.selectHeyueById((Integer) map.get("id"));
        Map map_ = new HashMap();
        map_.put("member_id",member_hy.getMember_id());
        map_.put("amount",member_hy.getRepare_capital());
        map_.put("type", BillCode.HEYUE_APPLY_FAILD.getCode());
        map_.put("mark", "拒绝用户"+member_hy.getMember_id()+"申请合约"+member_hy.getRepare_capital()+"元");
        map_.put("link_id", map.get("id"));
        applicationEventPublisher.publishEvent((Integer)map.get("apply_state") == 3? new RefuseApplyHeYueEvent(map_):new PassApplyHeYueEvent(map_));
        return ret;
    }

    @Override
    public List selectMemberHeYueByCase(Map map) {
        return memberHeYueApplyMapper.selectMemberHeYueByCase(map);
    }

    @Override
    public int updatePassword(Map map) {

        map.put("password",new BCryptPasswordEncoder().encode((String)map.get("password")));
        return memberMapper.updatePassword(map);
    }

    @Override
    public String findpassword(Integer id) {
        return memberMapper.findpassword(id);
    }

    @Override
    public UserBank findUserBank(Integer uid) {
        return memberMapper.findUserBank(uid);
    }

    @Override
    public Boolean updateUserBank(UserBank userBank,UserBank userBank_) {
        if(userBank_ != null){
            return memberMapper.updateUserBank(userBank);
        }else{
            return memberMapper.addUserBank(userBank);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean applyWithdraw(Map map) {

        double withdraw_money = (double) map.get("withdraw_money");
        double actual_money  = withdraw_money - widraw_fee;
        double fee = widraw_fee;
        Member user = memberMapper.findMember((String) map.get("username"));
        if(user.getAmount().doubleValue()< actual_money){
           return false;
        }

        Map map_ = new HashMap();
        map_.put("member_id",map.get("uid"));
        map_.put("repare_capital",withdraw_money);
        map_.put("user_name",map.get("username"));
        map_.put("amount",withdraw_money);
        map_.put("type", BillCode.APPLY_WITHDRAW.getCode());
        map_.put("mark", "用户"+user.getUsername()+"提现"+withdraw_money+"元");
        map.put("actual_money",actual_money);
        map.put("fee",fee);
        //通知观察者执行事件
        int aBoolean = memberMapper.applyWithdraw(map);
        map_.put("link_id",map.get("id"));
        applicationEventPublisher.publishEvent(new applyWithdrawEvent(map_));
        return aBoolean > 0;
    }

    @Override
    public Boolean addBill(Map map) {
        return memberMapper.addBill(map);
    }

    @Override
    public Member findMemberById(Integer id) {
        return memberMapper.findMemberById(id);
    }

    @Override
    public int decreaseMemberAmount(Map map) {
        return memberMapper.decreaseMemberAmount(map);
    }

    @Override
    public int increaseMemberAmount(Map map) {

        return memberMapper.increaseMemberAmount(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void  renewHeYue(Integer member_heyue_id,Integer flag) {
        com.stock.models.MemberHeYueApply heyue = MemberHeYueApplyImpl.selectHeyueById(member_heyue_id);
        double rate = HeYueCaculateImpl.cal_interest_rate(heyue.getHeyue_id(),heyue.getLeverage_id());
        int base_num = HeYueCaculateImpl.cal_capitial_used_time(heyue.getHeyue_id());
        double leverage_money = heyue.getLeverage_money();
        double interest = HeYueCaculateImpl.cal_interest(rate, leverage_money, base_num);
        //Member member = findMemberById(heyue.getMember_id());
                if(flag == 1){
                    if(heyue.getApply_state() != 4){
                        Map map = new HashMap<>();
                        map.put("member_id",heyue.getMember_id());
                        map.put("amount",interest);
                        memberMapper.decreaseMemberAmount(map);
                        Member member1 =  memberMapper.findMemberById(heyue.getMember_id());
                        Map data = new HashMap<>();
                        data.put("member_id",heyue.getMember_id());
                        data.put("link_id",member_heyue_id);
                        data.put("mark","合约"+member_heyue_id+"续期花费"+new BigDecimal(interest).setScale(2, RoundingMode.HALF_UP).toString()+"元");
                        data.put("amount",interest);
                        data.put("after_amount",member1.getAmount());
                        data.put("type",BillCode.EXTENSION_FEE.getCode());
                        int i=  MemberHeYueApplyImpl.addMemberInterest(data);
                        memberMapper.addBill(data);
                    }
                }
    }

    @Override
    public List selectHistoryHeYue(Map<Object, Object> map) {
        return memberHeYueApplyMapper.selectHistoryHeYue(map);
    }

    @Override
    public Integer findMemberByInviteCode(String invite_code) {
        return memberMapper.findMemberByInviteCode(invite_code);
    }

    @Override
    public int register(registerForm data) {
        return memberMapper.register(data);
    }

    @Override
    public List getFundListByPage(Map map) {
        return memberMapper.getFundListByPage(map);
    }

    @Override
    public Map getMemberInfo(int id) {
        return memberMapper.getMemberInfo(id);
    }

    @Override
    public double findMemberInterestByCase(Map<String, Object> map) {
        return MemberHeYueApplyImpl.findMemberInterestByCase(map);
    }

    @Override
    public List getMemberList(Map map) {
        return memberMapper.getMemberListByPage(map);
    }

    @Override
    public int updateMemberState(Map map) {
        return memberMapper.updateMemberState(map);
    }

    @Override
    public Member findMemberByPhone(Object phone) {
        return memberMapper.findMemberByPhone(phone);
    }

    @Override
    public void updateMemberPassword(Map map) {
        memberMapper.updateMemberPassword(map);
    }
    @Override
    public void xuyue(Integer member_heyue_id){
        SimpleDateFormat formatter_  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        com.stock.models.MemberHeYueApply heyue = MemberHeYueApplyImpl.selectHeyueById(member_heyue_id);
        Date cur =new Date();
        Date endTime = Holiday.findEndTime(heyue.getCapital_used_time());
        String end_time = formatter_.format(endTime);
        long s = endTime.getTime()-cur.getTime();
        Map map = new HashMap();
        map.put("id",(Integer) member_heyue_id);
        map.put("end_time",end_time);
        map.put("start_time",formatter_.format(cur));
        MemberHeYueApplyImpl.updateHeYueEndTime(map);
        MemberHeYueApplyImpl.updateHeYueStartTime(map);
        if(heyue.getApply_state() != 4){
            orderProductor.send_heyue_delay((Integer) member_heyue_id,(int)s);
        }
    }

    @Override
    public void updateUserNickName(UserBank userBank) {
        memberMapper.updateUserNickName(userBank);
    }

    @Override
    public List findMemberInterestList(Map<String, Object> map) {
        return memberMapper.findMemberInterestListByPage(map);
    }

    @Override
    public double findMemberSumInterest(Map<String, Object> map) {
        return memberMapper.findMemberSumInterest(map);
    }

    @Override
    public List<UserBill> getFinaceListByPage(Map map) {
        return memberMapper.getfinaceListByPage(map);
    }

    @Override
    public void createMember(Map map) {
        memberMapper.createMember(map);
    }

    @Override
    public void addMemberFengKong(Map map) {
        memberMapper.addMemberFengKong(map);
    }

    @Override
    public Map findMemberFengKong(int id) {
        return memberMapper.findMemberFengKong(id);
    }


}
