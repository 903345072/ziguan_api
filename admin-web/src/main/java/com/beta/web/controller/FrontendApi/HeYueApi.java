package com.beta.web.controller.FrontendApi;

import com.beta.web.contextHolder.MemberHolder;
import com.mapper.frontend.MemberMapper;
import com.stock.models.frontend.Member;
import com.stock.models.frontend.nettyOrder;
import com.stock.service.*;

import com.stock.service.ServiceImpl.HeYueCaculateAdpterInterface;
import com.util.RetResponse;
import com.util.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/frontend")
public class HeYueApi {
    @Autowired
    @Qualifier("HeYueCaculateAdper")
    HeYueCaculateAdpterInterface HeYueCaculateImpl;
    @Autowired
    @Qualifier("sina")
    StockDataServiceAbstract sina;
    @Autowired
    OrderService orderService;
    @Autowired
    InterestService InterestImpl;

    @Autowired
    HeYueService HeYueImpl;

    @Autowired
    LeverageService LeverageImpl;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    MemberHeYueApply MemberHeYueApplyImpl;

    @Autowired
    MemberService MemberServiceImpl;

    @RequestMapping(value = "/heyue/caculate",method = RequestMethod.GET)
    public  RetResult getAll (@RequestParam(value = "deposit") double deposit,@RequestParam(value = "heyue_id") int heyue_id, @RequestParam(value = "leverage_id") int leverage_id)
    {
        HashMap<String,Object> hm = new HashMap<>();
        double leverage_money = HeYueCaculateImpl.cal_leverage_money(deposit, leverage_id);
        double total_capitial = HeYueCaculateImpl.cal_total_capitial(deposit,leverage_money);
        double deposit_ = HeYueCaculateImpl.cal_deposit(deposit);
        double loss_warning_line = HeYueCaculateImpl.cal_loss_warning_line(deposit,0.5,leverage_money);
        double loss_sell_line = HeYueCaculateImpl.cal_loss_sell_line(deposit,0.3, leverage_money);
        double interest_rate = HeYueCaculateImpl.cal_interest_rate(heyue_id,leverage_id);
        int capitial_used_time = HeYueCaculateImpl.cal_capitial_used_time(heyue_id);
        double interest = HeYueCaculateImpl.cal_interest(interest_rate,leverage_money,capitial_used_time);
        double repare_capitical = HeYueCaculateImpl.cal_repare_capitical(deposit,interest);
        double leverageName = HeYueCaculateImpl.getLeverageName(leverage_id);
        hm.put("leverage_money",leverage_money);
        hm.put("total_capitial",total_capitial);
        hm.put("deposit",deposit_);
        hm.put("loss_warning_line",loss_warning_line);
        hm.put("loss_sell_line",loss_sell_line);
        hm.put("interest_rate",interest_rate);
        hm.put("interest",interest);
        hm.put("repare_capitical",repare_capitical);
        hm.put("leverage_name",leverageName);
        hm.put("use_time",capitial_used_time);
        return RetResponse.makeOKRsp(hm);
    }

    @RequestMapping(value = "/closeHeYue",method = RequestMethod.POST)
    public  RetResult coloseHeYue(@RequestBody Map maps){
    Integer id = (Integer) maps.get("id");
        //如果有持仓或者委托撤单，不能关闭  1
        Integer activeOrder =0;
        activeOrder = orderService.findActiveOrder(id);
        if(activeOrder >0){
            return RetResponse.makeErRsp("无法关闭，有持仓或委托、撤销中的订单");
        }
        com.stock.models.MemberHeYueApply memberHeYueApply = MemberHeYueApplyImpl.selectHeyueById(id);
        double init_captial = HeYueCaculateImpl.cal_total_capitial(memberHeYueApply.getDeposit(), memberHeYueApply.getLeverage_money());
        double profit= memberHeYueApply.getTotal_capital()-init_captial;
            //合约提盈
            Map map = new HashMap();
            map.put("member_heyue_id",id);
            map.put("member_id",memberHeYueApply.getMember_id());
            map.put("profit",profit);
            map.put("amount",memberHeYueApply.getDeposit()+profit);
            MemberHeYueApplyImpl.closeHeYue(map);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/pickHeYueProfit",method = RequestMethod.POST)
    public RetResult pickHeYueProfit(@RequestBody Map maps){
        Integer id = (Integer) maps.get("id");

        com.stock.models.MemberHeYueApply memberHeYueApply = MemberHeYueApplyImpl.selectHeyueById(id);
        double init_captial = HeYueCaculateImpl.cal_total_capitial(memberHeYueApply.getDeposit(), memberHeYueApply.getLeverage_money());
        double profit= memberHeYueApply.getTotal_capital()-init_captial;
        if(profit <=0){
            return RetResponse.makeErRsp("可提盈额度为0");
        }

        //如果有持仓或者委托撤单，不能关闭  1
        Integer activeOrder =0;
        activeOrder = orderService.findActiveOrder(id);
        if(activeOrder >0){
            return RetResponse.makeErRsp("无法提盈，有持仓或委托、撤销中的订单");
        }
        //合约提盈
        Map map = new HashMap();
        map.put("member_heyue_id",id);
        map.put("member_id",memberHeYueApply.getMember_id());
        map.put("amount",profit);
        MemberHeYueApplyImpl.pickHeYueProfit(map);
        return RetResponse.makeOKRsp();
    }


    @RequestMapping(value = "/expandHeYue",method = RequestMethod.POST)
    public RetResult expandHeYue(@RequestBody Map maps){
        Integer id = (Integer) maps.get("id");
        double amount = (double) maps.get("amount");
        com.stock.models.MemberHeYueApply memberHeYueApply = MemberHeYueApplyImpl.selectHeyueById(id);
        //合约提盈
        Map map = new HashMap();
        map.put("heyue",memberHeYueApply);
        map.put("amount",amount);
        MemberHeYueApplyImpl.expandHeYue(map);
        return RetResponse.makeOKRsp();
    }


    @RequestMapping(value = "/getHeYueLeverage",method = RequestMethod.GET)
    public RetResult getHeYueLeverage()
    {

        HashMap<String,Object> hm = new HashMap<>();
        List<Map> validHeYueIdList = HeYueImpl.findValidHeYueIdList();
        List<Map> validLeverageIdList = LeverageImpl.findValidLeverageIdList();
        Member member = memberMapper.findMemberById(MemberHolder.getId());
        hm.put("validHeYueIdList",validHeYueIdList);
        hm.put("validLeverageIdList",validLeverageIdList);
        hm.put("amount",member.getAmount());
        return RetResponse.makeOKRsp(hm);
    }

    @RequestMapping(value = "/applyHeYue",method = RequestMethod.GET)
    public RetResult applyHeYue(@RequestParam(value = "deposit") double deposit,@RequestParam(value = "heyue_id") int heyue_id, @RequestParam(value = "leverage_id") int leverage_id)
    {
        HashMap<String,Object> hm = new HashMap<>();
        double leverage_money = HeYueCaculateImpl.cal_leverage_money(deposit, leverage_id);
        double total_capitial = HeYueCaculateImpl.cal_total_capitial(deposit,leverage_money);
        double deposit_ = HeYueCaculateImpl.cal_deposit(deposit);
        double loss_warning_line = HeYueCaculateImpl.cal_loss_warning_line(deposit,0.5,leverage_money);
        double loss_sell_line = HeYueCaculateImpl.cal_loss_sell_line(deposit,0.3, leverage_money);
        double interest_rate = HeYueCaculateImpl.cal_interest_rate(heyue_id,leverage_id);
        int capitial_used_time = HeYueCaculateImpl.cal_capitial_used_time(heyue_id);
        double interest = HeYueCaculateImpl.cal_interest(interest_rate,leverage_money,capitial_used_time);
        double repare_capitical = HeYueCaculateImpl.cal_repare_capitical(deposit,interest);
        double bei = HeYueCaculateImpl.getLeverageName(leverage_id);
        int member_id = MemberHolder.getId();
        hm.put("leverage_money",leverage_money);
        hm.put("member_id",member_id);
        hm.put("total_capital",total_capitial);
        hm.put("deposit",deposit_);
        hm.put("loss_warning_line",loss_warning_line);
        hm.put("loss_sell_line",loss_sell_line);
        hm.put("interest_rate",interest_rate);
        hm.put("interest",interest);
        hm.put("repare_capital",repare_capitical);
        hm.put("amount",repare_capitical);
        hm.put("capital_used_time",capitial_used_time);
        hm.put("apply_state",0);
        hm.put("bei",bei);
        hm.put("heyue_id",heyue_id);
        hm.put("leverage_id",leverage_id);
        try {
            int i = MemberHeYueApplyImpl.addOneHeYueApply(hm);
            if(i == 1){
                return RetResponse.makeOKRsp();
            }
        }catch (Exception e){
           e.printStackTrace();
        }
        return RetResponse.makeErRsp();
    }

    @RequestMapping(value = "/selectMemberHeYueByCase")
    public RetResult selectMemberHeYueByCase(@RequestParam(value = "apply_state",defaultValue = "1") double apply_state){
        Map<Object, Object> map = new HashMap<>();
        map.put("member_id",MemberHolder.getId());
        map.put("apply_state",apply_state);
        List<com.stock.models.MemberHeYueApply> map1 = MemberServiceImpl.selectMemberHeYueByCase(map);
        map1.forEach(s->{
            double l = s.getOrders().stream().mapToDouble(d ->  sina.setDataSource(d.getStock_code()).getStockPrice().multiply(new BigDecimal(d.getBuy_hand())).doubleValue()).sum();
            s.setOrder_sum(new BigDecimal(l).setScale(2, RoundingMode.HALF_UP).doubleValue());
        });
        return RetResponse.makeOKRsp(map1);
    }

    @RequestMapping(value = "/selectHistoryHeYue")
    public RetResult selectHistoryHeYue(@RequestParam(value = "apply_state",defaultValue = "1") double apply_state){
        Map<Object, Object> map = new HashMap<>();
        map.put("member_id",MemberHolder.getId());
        map.put("apply_state",apply_state);
        List map1 = MemberServiceImpl.selectHistoryHeYue(map);
        return RetResponse.makeOKRsp(map1);
    }

    @RequestMapping(value = "/addDeposit",method = RequestMethod.POST)
    public RetResult addDeposit(@RequestBody com.stock.models.form.addDeposit map) throws Exception {
        map.setMember_id(MemberHolder.getId());
        Member member = MemberServiceImpl.findMemberById(MemberHolder.getId());
        if(map.getDeposit().compareTo(member.getAmount()) > 0){
           return RetResponse.makeErRsp("余额不足");
        }
        int i = 0;

        i = MemberHeYueApplyImpl.addDeposit(map);
        return RetResponse.makeOKRsp(i);
    }
    @RequestMapping(value = "/getRankList")
    public RetResult getHeYueProfitRankList(){
         List<com.stock.models.MemberHeYueApply> s =  MemberHeYueApplyImpl.getHeYueProfitRankList();
         s.forEach(d->{
             List<nettyOrder> activeOrderMoney = d.getOrder_list();
             double l = activeOrderMoney.stream().mapToDouble(p -> sina.setDataSource(p.getStock_code()).getStockPrice().doubleValue() * p.getBuy_hand()).sum();
             BigDecimal mv = BigDecimal.valueOf(d.getTotal_capital()).add(new BigDecimal(l)).setScale(2,RoundingMode.HALF_UP);
             BigDecimal profit = mv.subtract(BigDecimal.valueOf(d.getDeposit())).subtract(BigDecimal.valueOf(d.getLeverage_money())).setScale(2,RoundingMode.HALF_UP);
             d.setProfit_(profit.doubleValue());
             BigDecimal profit_rate = new BigDecimal(String.valueOf(profit)).setScale(2,RoundingMode.HALF_UP).divide(new BigDecimal(String.valueOf(d.getDeposit())),2,RoundingMode.HALF_UP);
             BigDecimal sd =  new BigDecimal(String.valueOf(profit_rate)).setScale(2,RoundingMode.HALF_UP);
             d.setProfit_rate(sd.doubleValue());
         });
        List<com.stock.models.MemberHeYueApply> collect = s.stream().filter(d -> {
            if (d.getProfit_() > 0) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        Collections.sort(collect);
        return RetResponse.makeOKRsp(collect);
    }

}
