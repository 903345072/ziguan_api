package com.stock.service.ServiceImpl;

import com.mapper.frontend.MemberHeYueApplyMapper;
import com.mapper.frontend.MemberMapper;
import com.stock.models.frontend.Member;
import com.stock.service.MemberHeYueApply;
import com.stock.service.MemberService;
import com.util.BillCode;
import event.addDepositEvent;
import com.util.publishEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MemberHeYueApplyImpl implements MemberHeYueApply {

    @Autowired
    MemberHeYueApplyMapper memberHeYueApplyMapper;

    @Autowired
    @Qualifier("HeYueCaculateAdper")
    HeYueCaculateAdpterInterface HeYueCaculateImpl;
    @Autowired
    MemberMapper memberMapper;

    @Autowired
    publishEvent p;

    @Autowired
    MemberService memberService;

    @Transactional(rollbackFor=Exception.class)
    @Override
    public int addOneHeYueApply(Map map) {
            int i = memberHeYueApplyMapper.addOneHeYueApply(map);
            int j = memberMapper.decreaseMemberAmount(map);
            Member member = memberMapper.findMemberById((Integer) map.get("member_id"));
            Map map_ = new HashMap();
            map_.put("member_id",map.get("member_id"));
            map_.put("link_id",map.get("id"));
            map_.put("amount",map.get("repare_capital"));
            map_.put("type", BillCode.APPLY_HEYUE.getCode());
            map_.put("after_amount", member.getAmount());
            map_.put("mark", "用户"+member.getId()+"申请合约"+map.get("id")+"消费资金"+map.get("repare_capital")+"元");
            memberMapper.addBill(map_);

            if(i<=0 || j<=0){
                return 0;
            }
            return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addDeposit(com.stock.models.form.addDeposit map) throws Exception {
        int ret = 0;
        int ret1 = 0;
        Map data = new HashMap();
        com.stock.models.MemberHeYueApply memberHeYueApply = memberHeYueApplyMapper.selectHeyueById(map.getId());
        BigDecimal deposit = map.getDeposit().add(new BigDecimal(memberHeYueApply.getDeposit()));
        double loss_sell_line = HeYueCaculateImpl.cal_loss_sell_line(deposit.doubleValue(), 0.3, memberHeYueApply.getLeverage_money());
        double loss_warning_line = HeYueCaculateImpl.cal_loss_warning_line(deposit.doubleValue(), 0.5, memberHeYueApply.getLeverage_money());
        double cal_total_capitial = HeYueCaculateImpl.cal_total_capitial(deposit.doubleValue(),memberHeYueApply.getLeverage_money());
        Map map_ = new HashMap();
        map_.put("deposit",deposit);
        map_.put("add_deposit",map.getDeposit());
        map_.put("id",map.getId());
        map_.put("loss_warning_line",loss_warning_line);
        map_.put("loss_sell_line",loss_sell_line);
        map_.put("total_capital",cal_total_capitial);
        ret = memberHeYueApplyMapper.addDeposit(map_);
        data.put("member_heyue_id",map.getId());

        data.put("amount",map.getDeposit().multiply(new BigDecimal(memberHeYueApply.getBei())));

        com.stock.models.MemberHeYueApply mh =   memberHeYueApplyMapper.selectHeyueById(map.getId());
        Map bill = new HashMap();
        bill.put("member_id",map.getMember_id());
        bill.put("link_id",map.getId());
        bill.put("mark","用户"+map.getMember_id()+"合约"+map.getId()+"增加保证金"+data.get("amount").toString()+"元");
        bill.put("amount",data.get("amount"));
        bill.put("after_amount",mh.getTotal_capital());
        bill.put("type", BillCode.ADD_TO_DEPOSIT.getCode());
        memberService.addBill(bill);
        //增加账单
       p.packageBillData(map.getMember_id(), map.getDeposit(), BillCode.ADD_TO_DEPOSIT_HEYUE.getCode(), "用户" + map.getMember_id() + "增加保证金" + map.getDeposit() + "元", map.getId()).send(addDepositEvent.class);
       return ret;
    }

    @Override
    public com.stock.models.MemberHeYueApply selectHeyueById(Integer id) {
        return memberHeYueApplyMapper.selectHeyueById(id);
    }

    @Override
    public int decreaseHeYueMoney(Map map) {
        return memberHeYueApplyMapper.decreaseHeYueMoney(map);
    }

    @Override
    public int updateHeYueStartTime(Map map) {
        return memberHeYueApplyMapper.updateHeYueStartTime(map);
    }

    @Override
    public int updateHeYueApplyState(Map map) {
        return memberHeYueApplyMapper.updateHeYueApplyState(map);
    }

    @Override
    public int updateHeYueEndTime(Map map) {
        return memberHeYueApplyMapper.updateHeYueEndTime(map);
    }

    @Override
    public List selectMemberHeYueByStates() {
        return memberHeYueApplyMapper.selectMemberHeYueByStates();
    }

    @Override
    public int increaseHeYueMoney(Map map) {
        return memberHeYueApplyMapper.increaseHeYueMoney(map);
    }

    @Override
    public int decreaseHeYueProfit(Map map) {
        return memberHeYueApplyMapper.decreaseHeYueProfit(map);
    }

    @Override
    public int increaseHeYueProfit(Map map) {
        return memberHeYueApplyMapper.increaseHeYueProfit(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int closeHeYue(Map map) {
//            if((double)map.get("profit") > 0){
//                Map d = new HashMap<>();
//                d.put("amount",map.get("profit"));
//                d.put("member_heyue_id",map.get("member_heyue_id"));
//                decreaseHeYueMoney(d);
//            }
            memberService.increaseMemberAmount(map);
            Member member = memberService.findMemberById((Integer) map.get("member_id"));

            Map bill = new HashMap();
            bill.put("member_id",map.get("member_id"));
            bill.put("link_id",map.get("member_heyue_id"));
            bill.put("mark","用户"+map.get("member_id")+"关闭合约"+map.get("member_heyue_id")+"获取"+new BigDecimal((double)map.get("amount")).setScale(2,RoundingMode.HALF_UP)+"元");
            bill.put("amount",map.get("amount"));
            bill.put("after_amount",member.getAmount());
            bill.put("type", BillCode.HEYUE_BALANCE.getCode());
            memberService.addBill(bill);

        memberHeYueApplyMapper.updateHeYueCloseDate((Integer)map.get("member_heyue_id"));
        Map s = new HashMap();
        s.put("id",map.get("member_heyue_id"));
        s.put("apply_state",4);
        return memberHeYueApplyMapper.updateHeYueApplyState(s);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pickHeYueProfit(Map map) {
        decreaseHeYueMoney(map);
        memberService.increaseMemberAmount(map);
        Member member = memberService.findMemberById((Integer) map.get("member_id"));
        Map bill = new HashMap();
        bill.put("member_id",map.get("member_id"));
        bill.put("link_id",map.get("member_heyue_id"));
        bill.put("mark","用户"+map.get("member_id")+"合约"+map.get("member_heyue_id")+"提盈"+new BigDecimal((double)map.get("amount")).setScale(2, RoundingMode.HALF_UP)+"元");
        bill.put("amount",map.get("amount"));
        bill.put("after_amount",member.getAmount());
        bill.put("type", BillCode.HEYUE_PROFIT.getCode());
        memberService.addBill(bill);
       return 1;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int expandHeYue(Map map) {
        com.stock.models.MemberHeYueApply heyue = (com.stock.models.MemberHeYueApply) map.get("heyue");
        double amount = (double) map.get("amount");

        double leverage_money = HeYueCaculateImpl.cal_leverage_money(amount,heyue.getLeverage_id());

        double total_capitial = HeYueCaculateImpl.cal_total_capitial(amount, leverage_money);

        double loss_warning_line = HeYueCaculateImpl.cal_loss_warning_line(amount, 0.5, leverage_money);

        double loss_sell_line = HeYueCaculateImpl.cal_loss_sell_line(amount, 0.3, leverage_money);
        double rate = HeYueCaculateImpl.cal_interest_rate(heyue.getHeyue_id(), heyue.getLeverage_id());
        double interest = HeYueCaculateImpl.cal_interest(rate, leverage_money, heyue.getCapital_used_time());
        double money = amount+interest;
        Member member_ = memberService.findMemberById(heyue.getMember_id());
        if(member_.getAmount().doubleValue() < money){
            throw new RuntimeException("余额不足");
        }



        map.put("total_capital",total_capitial);
        map.put("deposit",amount);
        map.put("loss_warning_line",loss_warning_line);
        map.put("loss_sell_line",loss_sell_line);
        map.put("interest",interest);
        map.put("leverage_money",leverage_money);
        map.put("id",heyue.getId());
        map.put("repare_capital",money);
        int i = memberHeYueApplyMapper.expandHeYue(map);

        Map data = new HashMap();
        data.put("amount",money);
        data.put("member_id",heyue.getMember_id());
        memberService.decreaseMemberAmount(data);
        Member member = memberService.findMemberById(heyue.getMember_id());
        Map bill = new HashMap();
        bill.put("member_id",heyue.getMember_id());
        bill.put("link_id",heyue.getId());
        bill.put("mark","用户"+heyue.getMember_id()+"扩大合约"+heyue.getId()+"花费"+money+"元");
        bill.put("amount",money);
        bill.put("after_amount",member.getAmount());
        bill.put("type", BillCode.EXPAND_HEYUE.getCode());
        memberService.addBill(bill);


        Map ii = new HashMap();
        ii.put("member_id",heyue.getMember_id());
        ii.put("link_id",heyue.getId());
        ii.put("mark","用户"+heyue.getMember_id()+"扩大合约"+heyue.getId()+"利息花费"+interest+"元");
        ii.put("amount",interest);
        ii.put("after_amount",member.getAmount());
        ii.put("type", BillCode.EXTENSION_FEE.getCode());
        memberService.addBill(ii);


        Map datas = new HashMap();
        datas.put("link_id",heyue.getId());
        datas.put("amount",interest);
        addMemberInterest(datas);
        return i;
    }

    @Override
    public List findCanEffectHeYue() {
        return memberHeYueApplyMapper.findCanEffectHeYue();
    }

    @Override
    public List getHeYueProfitRankList() {
        return memberHeYueApplyMapper.getHeYueProfitRankList();
    }

    @Override
    public int addMemberInterest(Map data) {
        return memberHeYueApplyMapper.addMemberInterest(data);
    }

    @Override
    public double findMemberInterestByCase(Map<String, Object> map) {
        return memberHeYueApplyMapper.findMemberInterestByCase(map);
    }

    @Override
    public void addForceLog(Map force) {
        memberHeYueApplyMapper.addForceLog(force);
    }

    @Override
    public void addHeYue(Map map) {
        memberHeYueApplyMapper.addHeYue(map);
    }

    @Override
    public void updateMemberHeYue(Map map) {
        memberHeYueApplyMapper.updateMemberHeYue(map);
    }


}
