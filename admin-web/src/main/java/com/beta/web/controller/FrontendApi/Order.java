package com.beta.web.controller.FrontendApi;

import com.alibaba.fastjson.JSON;
import com.beta.web.contextHolder.MemberHolder;
import com.beta.web.service.UserSetter;
import com.mapper.frontend.MemberMapper;
import com.stock.models.frontend.Member;
import com.stock.models.frontend.nettyOrder;
import com.stock.service.*;
import com.stock.service.ServiceImpl.HeYueCaculateAdpterInterface;
import com.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frontend")
public class Order {


    @Autowired
    @Qualifier("sina")
    StockDataServiceAbstract SinaStockServiceImpl;

    @Autowired
    OrderService OrderServiceImpl;

    @Autowired
    MemberHeYueApply MemberHeYueApplyImpl;

    @RequestMapping(value = "/order/makeOrder",method = RequestMethod.POST)
    public RetResult makeOrder(@Valid @RequestBody com.stock.models.frontend.Order order, BindingResult result){

        int id = MemberHolder.getId();
        order.setMember_id(id);
        //远程下单
//        String entrust = (String) YlTrade.entrust();
//        Map parse = (Map) JSON.parse(entrust);
//        Integer state = (Integer) parse.get("state");
//        if (!state.equals(1)){
//            return RetResponse.makeErRsp((String) parse.get("message"));
//        }
//        order.setContract_no((String) parse.get("value"));

        //中小板(002开头)股票涨跌超过-+8无法买入
        String substring = order.getStock_code().substring(0, 5);
        boolean st1 = order.getStock_name().contains("ST");
        boolean st2 = order.getStock_name().contains("st");
        boolean N1 = order.getStock_name().contains("N");
        boolean N2 = order.getStock_name().contains("n");
        if(st1 || st2){
            return RetResponse.makeErRsp("禁止交易带有ST股票");
        }
        if(N1 || N2){
            return RetResponse.makeErRsp("禁止交易股改、重组、增发类型股票");
        }
        if(substring.equals("sz002")){
            BigDecimal stockRate = SinaStockServiceImpl.setDataSource(order.getStock_code()).getStockRate();
            if(stockRate.doubleValue()< -8 || stockRate.doubleValue()>8){
                return RetResponse.makeErRsp("中小板涨跌幅超过+-8%禁止买入");
            }
        }
        if(substring.equals("sz300")){
            BigDecimal stockRate = SinaStockServiceImpl.setDataSource(order.getStock_code()).getStockRate();
            if(stockRate.doubleValue()< -15 || stockRate.doubleValue()>15){
                return RetResponse.makeErRsp("创业板涨跌幅超过+-15%禁止买入");
            }
        }
        Map map = new HashMap<>();
        map.put("heyue_id",order.getMember_heyue_id());
        map.put("member_id",order.getMember_id());
        map.put("stock_code",order.getStock_code());
        nettyOrder parentOrder = OrderServiceImpl.findParentOrder(map);
        if(parentOrder != null){
            //pid没有持仓

            //如果已撤的父订单也不能作为父订单
            order.setPid(parentOrder.getId());
        }
        //根据实盘委托状态判断是否能够下单
        if(order.getEntrust_way() == 2){
            try {
                order.setBuy_price(SinaStockServiceImpl.setDataSource(order.getStock_code()).getStockPrice().doubleValue());
            }catch (Exception e){
                return RetResponse.makeErRsp("获取股价失败,请重试");
            }
        }else{
            if(order.getBuy_price() <=0){
                return RetResponse.makeErRsp("请填写股价");
            }
        }
        //合约是否有效
        com.stock.models.MemberHeYueApply memberHeYueApply = MemberHeYueApplyImpl.selectHeyueById(order.getMember_heyue_id());
        if(memberHeYueApply.getApply_state() != 1){
            return RetResponse.makeErRsp("此合约已失效");
        }
        //操盘资金是否足够
        BigDecimal hand = BigDecimal.valueOf(order.getBuy_hand());
        BigDecimal price = BigDecimal.valueOf(order.getBuy_price());
        if(hand.multiply(price).compareTo(BigDecimal.valueOf(memberHeYueApply.getTotal_capital())) > 0){
            return RetResponse.makeErRsp("此合约可用资金不足");
        }
        List<nettyOrder> order_list = memberHeYueApply.getOrder_list();
        double l = order_list.stream().mapToDouble(d -> SinaStockServiceImpl.setDataSource(d.getStock_code()).getStockPrice().doubleValue() * d.getBuy_hand()).sum();
        double total_captial = l+memberHeYueApply.getTotal_capital();
        if(total_captial < memberHeYueApply.getLoss_sell_line()){
            return RetResponse.makeErRsp("合约总资产已低于平仓线！");
        }
        //开始委托

        order.setPing_way(0);
        int i = OrderServiceImpl.makerOrder(order);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/order/apply_cancel",method = RequestMethod.POST)
    public RetResult apply_cancel(@RequestBody Map map){
        Integer id = (Integer) map.get("id");
        nettyOrder order = OrderServiceImpl.findOrderById(id);
        if(order.getCancel_status() != 0 ){
            return RetResponse.makeErRsp("此单已经在撤中");
        }
        if(order.getPing_way() == 1 ){
            return RetResponse.makeErRsp("系统强制委托,无法撤销");
        }
        if(order.getStock_status() != 1 ){
            return RetResponse.makeErRsp("非委托中的单不能撤销");
        }
        int i = OrderServiceImpl.apply_cancel(id);
        return RetResponse.makeOKRsp(i);
    }



    @RequestMapping(value = "/order/sell_stock",method = RequestMethod.POST)
    public RetResult sell_stock(@Valid @RequestBody com.stock.models.frontend.Order order, BindingResult result){
        //先看是否持有该股，根据合约和股票代码查询
        int id = MemberHolder.getId();
        order.setMember_id(id);
       Map map = new HashMap<>();
       map.put("heyue_id",order.getMember_heyue_id());
       map.put("member_id",order.getMember_id());
       map.put("stock_code",order.getStock_code());
        nettyOrder parentOrder = OrderServiceImpl.findParentOrder(map);
        if(parentOrder == null){
            return RetResponse.makeErRsp("您没有该股持仓");
        }
        Map data = new HashMap<>();
        data.put("member_id",id);
        data.put("stock_code",order.getStock_code());
        data.put("member_heyue_id",order.getMember_heyue_id());
        Integer tc = OrderServiceImpl.findTodayHoldCount(data);

        order.setPid(parentOrder.getId());

        if((parentOrder.getBuy_hand()-tc)<order.getBuy_hand()){
            return RetResponse.makeErRsp("可用数量不够");
        }
        order.setPing_way(0);
        OrderServiceImpl.makerOrder(order);
        return RetResponse.makeOKRsp();
    }
    @RequestMapping(value = "/order/findOrderByCase")
    public RetResult findOrderByState(@RequestParam Map map){
        List order_list = OrderServiceImpl.findOrderByCase(map);

        if(Integer.parseInt((String) map.get("stock_status")) == 2){
            order_list.forEach(s->{
                nettyOrder order = (nettyOrder) s;
                Map map_ = new HashMap();
                map_.put("stock_code",order.getStock_code());
                map_.put("member_id",order.getMember_id());
                map_.put("member_heyue_id",order.getMember_heyue_id());
                Integer todayHoldCount = OrderServiceImpl.findTodayHoldCount(map_);

                int can_sell = order.getBuy_hand()-todayHoldCount-order.getFreeze_hand();
                if(can_sell <0){
                    can_sell = 0;
                }
                order.setCan_sell(can_sell);
                OrderServiceImpl.setOrder(order);
            });
        }

        return RetResponse.makeOKRsp(order_list);
    }
}
