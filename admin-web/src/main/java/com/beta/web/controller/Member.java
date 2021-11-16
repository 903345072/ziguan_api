package com.beta.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beta.web.contextHolder.MemberHolder;
import com.mapper.frontend.MemberMapper;
import com.stock.models.MemberHeYueApply;
import com.stock.models.Recharge;
import com.stock.models.form.registerForm;
import com.stock.models.frontend.UserBank;
import com.stock.models.frontend.nettyOrder;
import com.stock.service.MemberService;
import com.stock.service.RechargeService;
import com.stock.service.StockDataServiceAbstract;
import com.util.BillCode;
import com.util.RetResponse;
import com.util.RetResult;
import com.util.smsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/member")
public class Member {
    @Autowired
    private RedisTemplate redisTemplate_;
    @Autowired
    MemberService MemberServiceImpl;
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    RechargeService RechargeServiceImpl;
    @Autowired
    com.stock.service.MemberHeYueApply memberHeYueApplyImpl;

    @Autowired
    @Qualifier("sina")
    StockDataServiceAbstract sina;

    @RequestMapping("/getMemberInterestList")
    public RetResult getMemberInterestList(@RequestParam Map<String ,Object> map){
        int id = MemberHolder.getId();
        if(id != 60){
            map.put("pid",id);
        }
        Map<String,Object> data = new HashMap<>();
        List memberInterestList =  MemberServiceImpl.findMemberInterestList(map);
        double interets = MemberServiceImpl.findMemberSumInterest(map);

        data.put("items",memberInterestList.get(0));
        data.put("count",memberInterestList.get(1));
        data.put("total_interest",interets);
        return RetResponse.makeOKRsp(data);
    }

    @RequestMapping("/getMemberHeYueApplyList")
    public RetResult getAll(@RequestParam Map<String ,Object> map)
    {
        int id = MemberHolder.getId();
        if(id != 60){
            map.put("pid",id);
        }
       Map<String,Object> data = new HashMap<>();
       List memberHeYueApplyList = MemberServiceImpl.findMemberHeYueApplyList(map);

       double member_interets = MemberServiceImpl.findMemberInterestByCase(map);
       List<MemberHeYueApply> ml = (List) memberHeYueApplyList.get(0);
       ml.forEach(s->{
           List<nettyOrder> activeOrderMoney = s.getOrder_list();
           double l = activeOrderMoney.stream().mapToDouble(d -> sina.setDataSource(d.getStock_code()).getStockPrice().doubleValue() * d.getBuy_hand()).sum();
           BigDecimal mv = new BigDecimal(s.getTotal_capital()).add(new BigDecimal(l)).setScale(2,RoundingMode.HALF_UP);
           s.setMarket_value(mv);
       });
       data.put("items",ml);
       data.put("count",memberHeYueApplyList.get(1));
       data.put("member_interets",member_interets);
       return RetResponse.makeOKRsp(data);
    }

    @RequestMapping(value = "/updateHeYueApplyState",method = RequestMethod.POST)
    public RetResult updateHeYueApplyState(@RequestBody Map<String ,Object> map)
    {
        int ret = MemberServiceImpl.updateHeYueApplyState(map);
        if(ret>0){
            return RetResponse.makeOKRsp(ret);
        }
        return RetResponse.makeErRsp();
    }

    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    public RetResult updatePassword(@RequestBody Map<String ,Object> map)
    {
        map.put("id",MemberHolder.getId());

        String true_password = MemberServiceImpl.findpassword(MemberHolder.getId());
        if(!new BCryptPasswordEncoder().matches((String)map.get("old_pwd"),true_password)){
            return RetResponse.makeErRsp("原始密码错误");
        }

        int ret = MemberServiceImpl.updatePassword(map);

        if(ret>0){
            return RetResponse.makeOKRsp(ret);
        }
        return RetResponse.makeErRsp();
    }

    @RequestMapping(value = "/findUserBank")
    public RetResult findUserBank()
    {
        UserBank userBank = MemberServiceImpl.findUserBank(MemberHolder.getId());
            return RetResponse.makeOKRsp(userBank);
    }

    @RequestMapping(value = "/updateUserBank",method = RequestMethod.POST)
    public RetResult updateUserBank(@RequestBody UserBank userBank)
    {
        userBank.setUid(MemberHolder.getId());
        UserBank userBank_ = MemberServiceImpl.findUserBank(MemberHolder.getId());

        MemberServiceImpl.updateUserNickName(userBank);
        Boolean res =  MemberServiceImpl.updateUserBank(userBank,userBank_);

        return RetResponse.makeOKRsp(res);
    }

    @RequestMapping(value = "/findMember")
    public RetResult findMember()
    {
        com.stock.models.frontend.Member member = memberMapper.findMember(MemberHolder.getUsername());
        return RetResponse.makeOKRsp(member);
    }

    @RequestMapping(value = "/giveMoney",method = RequestMethod.POST)
    public RetResult giveMoney(@RequestBody Map data)
    {
        Recharge recharge = new Recharge();

        recharge.setState(1);
        recharge.setType(4);
        recharge.setUid((Integer) data.get("member_id"));
        recharge.setAmount(new BigDecimal((String) data.get("amount")));
        int dorecharge = RechargeServiceImpl.dorecharge(recharge);
        MemberServiceImpl.increaseMemberAmount(data);
        final com.stock.models.frontend.Member member = MemberServiceImpl.findMemberById((Integer) data.get("member_id"));
        Map bill = new HashMap<>();
        bill.put("member_id",data.get("member_id"));
        bill.put("link_id",dorecharge);
        bill.put("mark","用户"+data.get("member_id")+"手动充值"+data.get("amount"));
        bill.put("amount",data.get("amount"));
        bill.put("after_amount",member.getAmount());
        bill.put("type", BillCode.RECHARGE_BY_MAN.getCode());
        MemberServiceImpl.addBill(bill);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/applyWithdraw",method = RequestMethod.POST)
    public RetResult applyWithdraw(@RequestBody Map<String ,Object> map)
    {

        map.put("uid",MemberHolder.getId());
        map.put("username",MemberHolder.getUsername());
        Boolean aBoolean = MemberServiceImpl.applyWithdraw(map);
        if(!aBoolean){
            return RetResponse.makeErRsp("余额不足");
        }
        return RetResponse.makeOKRsp(aBoolean);
    }

    @RequestMapping(value = "/register/senCode")
    public RetResult sendSms(@RequestParam String phone)
    {
        Object o = redisTemplate_.opsForValue().get(phone);

        if(o != null){
            return RetResponse.makeErRsp("60s内请勿重复发送");
        }
        int i = (int)(Math.random()*(9999-1000+1))+1000;
        String s = String.valueOf(i);
        String jsonObject = smsUtil.sendSms(phone,s);
        if(!jsonObject.equals("0")){
            return RetResponse.makeErRsp("发送失败");
        }
        redisTemplate_.opsForValue().set(phone,s,60, TimeUnit.SECONDS);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/register/forget_password",method = RequestMethod.POST)
    public RetResult forget_password(@RequestBody Map map){
        //手机号不存在gg

        com.stock.models.frontend.Member m  =  MemberServiceImpl.findMemberByPhone(map.get("phone"));

        if(m == null){
            return RetResponse.makeErRsp("此用户不存在");
        }
        String o = (String) redisTemplate_.opsForValue().get(map.get("phone"));
        if(o == null){
            return RetResponse.makeErRsp("邀请码已失效请重新发送");
        }
        if(!o.equals(map.get("captcha"))){
            return RetResponse.makeErRsp("验证码错误");
        }
        map.put("password",new BCryptPasswordEncoder().encode((String) map.get("password")));
        MemberServiceImpl.updateMemberPassword(map);
        return RetResponse.makeOKRsp();
    }
    @RequestMapping("/getFundList")
    public RetResult getFundList(@RequestParam Map map){
        map.put("uid",MemberHolder.getId());

        List data = MemberServiceImpl.getFundListByPage(map);

        return RetResponse.makeOKRsp(data);
    }

    @RequestMapping("/getMemberInfo")
    public RetResult getMemberInfo(){

        Map data = MemberServiceImpl.getMemberInfo(MemberHolder.getId());
        return RetResponse.makeOKRsp(data);
    }

    @RequestMapping("/getMemberList")
    public RetResult getMemberList(@RequestParam Map map){
        Map<String,Object> data = new HashMap<>();
        int id = MemberHolder.getId();
        if(id != 60){
            map.put("pid",id);
        }
        List lst = MemberServiceImpl.getMemberList(map);
       List<com.stock.models.frontend.Member> m =  (List<com.stock.models.frontend.Member>)lst.get(0);
       m.forEach(s->{
           if(s.getFengkong() == null){
              Map ms = new HashMap();
              ms.put("yj_rate",10);
              ms.put("is_kc",1);
              ms.put("kc_max",10000);
              ms.put("single_max",10000);
              ms.put("forbid_list","");
               s.setFengkong(ms);
           }
       });
        data.put("items",lst.get(0));
        data.put("count",lst.get(1));
        return RetResponse.makeOKRsp(data);
    }

    @RequestMapping("/createMember")
    public RetResult createMember(@RequestBody Map map){
        map.put("password",new BCryptPasswordEncoder().encode((CharSequence) map.get("password")));
        MemberServiceImpl.createMember(map);
        memberHeYueApplyImpl.addHeYue(map);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping("/updateMemberFengkong")
    public RetResult updateMemberFengkong(@RequestBody Map map){

        MemberServiceImpl.addMemberFengKong(map);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping("/updateMemberHeYue")
    public RetResult updateMemberHeYue(@RequestBody Map map){
        memberHeYueApplyImpl.updateMemberHeYue(map);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/updateMemberState",method = RequestMethod.POST)
    public RetResult updateMemberState(@RequestBody Map map){
        int i = MemberServiceImpl.updateMemberState(map);
        return RetResponse.makeOKRsp();
    }
}
