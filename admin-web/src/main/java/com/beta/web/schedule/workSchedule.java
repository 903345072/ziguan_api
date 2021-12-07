package com.beta.web.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mapper.frontend.OrderMapper;
import com.stock.models.Chengjiao;
import com.stock.models.broker;
import com.stock.models.frontend.Member;
import com.stock.models.frontend.nettyOrder;
import com.stock.service.*;
import com.stock.service.ServiceImpl.OrderServiceImpl;
import com.sun.corba.se.pept.broker.Broker;
import com.util.BillCode;
import com.util.RetResponse;
import com.util.TdxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@Component
@EnableScheduling
public class workSchedule {

    @Autowired
    BrokerService brokerService;
    @Autowired
    MemberHeYueApply memberHeYueApply;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderService orderServiceImpl;
    @Autowired
    MemberService memberService;
    @Autowired
    @Qualifier("wwyy")
    StockDataServiceAbstract sina;
    @Autowired
    private RedisTemplate redisTemplate_;

    /**
     * 检查止盈止损
     */
    @Scheduled(cron = "0 0/1 9-14 * * ?")
    //@Scheduled(cron = "0/5 * * * * ?")
    //@Transactional(rollbackFor=Exception.class)
    public void checkStrategy() throws InterruptedException {

        List<com.stock.models.MemberHeYueApply> list = memberHeYueApply.selectMemberHeYueByStates();
        List<Map> chuQuan = brokerService.getChuQuan();
        List<String> stock_codes = chuQuan.stream().map(d -> (String)d.get("stock_code")).collect(Collectors.toList());
        list.forEach( s->{//and trade_direction = 1
            List<nettyOrder> activeOrderMoney = s.getOrder_list();
            double l = activeOrderMoney.stream().mapToDouble(d->{
                BigDecimal price = sina.setDataSource(d.getStock_code()).getStockPrice();
                if(stock_codes.contains(d.getStock_code())){
                    price = brokerService.findPriceByCode(d.getStock_code());
                }
                if(price == null){
                    return 10000000;
                }
                if(price.doubleValue()<=0 ){
                    return 10000000;
                }
                if(d.getStock_status() == 1){
                    return price.doubleValue() * d.getWeituo_hand();
                }
                return price.doubleValue() * d.getBuy_hand();
            }).sum();
            double total_cap = s.getTotal_capital() + l;
            if(total_cap < s.getLoss_sell_line()){
                //把持仓中的卖掉,委托买入的撤掉


                Map dd = new HashMap();
                dd.put("id",s.getId());
                List<nettyOrder> holdOrder = orderServiceImpl.findHoldOrder(dd);

                holdOrder.forEach(h->{
                    try {
                        if(h.getStock_status() == 2 && h.getPid() == 0){
                            //卖

                            nettyOrder sellOrder = new nettyOrder();
                            Map today_map = new HashMap<>();
                            today_map.put("member_heyue_id",h.getMember_heyue_id());
                            today_map.put("stock_code",h.getStock_code());
                            today_map.put("member_id",h.getMember_id());
                            Integer todayHoldCount = orderServiceImpl.findTodayHoldCount(today_map);
                            Integer hand = h.getBuy_hand()-todayHoldCount;
                            if(hand > 100){
                                sellOrder.setBuy_hand(hand);
                                sellOrder.setEntrust_way(2);
                                sellOrder.setTrade_direction(2);
                                sellOrder.setPid(h.getId());
                                sellOrder.setMember_heyue_id(h.getMember_heyue_id());
                                sellOrder.setStock_code(h.getStock_code());
                                sellOrder.setStock_name(h.getStock_name());
                                sellOrder.setStock_status(1);
                                sellOrder.setCancel_status(0);
                                sellOrder.setPing_way(1);
                                sellOrder.setMember_id(h.getMember_id());
                                BigDecimal stockPrice = sina.setDataSource(h.getStock_code()).getStockPrice();
                                sellOrder.setBuy_price(stockPrice.doubleValue());
                                sellOrder.setBroker_id(h.getBroker_id());
                                orderServiceImpl.makerOrder(sellOrder);
                                orderServiceImpl.updateOrderSystem(h.getId());
                            }
                            Map force = new HashMap();
                            force.put("member_heyue_id",s.getId());
                            force.put("total_cap_money",s.getTotal_capital());
                            force.put("loss_line",s.getLoss_sell_line());
                            force.put("market_value",l);
                            force.put("total_money",total_cap);
                            memberHeYueApply.addForceLog(force);
                        }else if(h.getStock_status() == 1 && h.getTrade_direction() ==1 && h.getCancel_status() ==0){
                            //撤买的
                            orderServiceImpl.updateOrderSystem(h.getId());
                            orderServiceImpl.apply_cancel(h.getId());
                        }
                    }catch (RuntimeException e){
                        e.printStackTrace();
                    }

                });
            }


            //根据当前合约下的订单盈亏判断是否能够平仓，或者达到预警线(发送一个预警提示信息)
        });
        //扫描合约
    }

    public BigDecimal get_sx_fee(BigDecimal hand,BigDecimal buy_price,BigDecimal rate){
       double rate_s = rate.doubleValue()/1000;
        BigDecimal v = hand.multiply(buy_price).multiply(new BigDecimal(rate_s));
        if(v.doubleValue() < 5){
            v = new BigDecimal(5);
        }
        return v;
    }
    public BigDecimal get_yh_fee(BigDecimal hand,BigDecimal buy_price){
        BigDecimal v = hand.multiply(buy_price).multiply(new BigDecimal(0.001));
        if(v.doubleValue() < 1){
            v = new BigDecimal(1);
        }
        return v;
    }

    //@Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0/15 * * * * ?")
    @Transactional(rollbackFor=Exception.class)
    public void checkWeituo(){
        List<broker> all = brokerService.getAll();
        all.forEach(p->{
                    List weituo_order = orderServiceImpl.findCanWeiTuo(p.getId());
                    if (weituo_order.size()>0){
                        weituo_order.forEach(e->{
                            nettyOrder order = (nettyOrder)e;

                            String stock_code = order.getStock_code().substring(2);
                            Map mm = new HashMap();
                            mm.put("contract_no",order.getContract_no());
                            mm.put("stock_code",stock_code);
                            List<Chengjiao> chengjiaos = orderServiceImpl.NoChengjiaoByContractNo(mm);//800
                            List<Chengjiao> no_cj = chengjiaos.stream().filter(k -> k.getStatus() == 0).collect(Collectors.toList());//100
                            Integer all_hand = chengjiaos.stream().mapToInt(Chengjiao::getHand).sum();//800
                            if(no_cj.size() > 0){
                                double cj_money = no_cj.stream().mapToDouble(s -> s.getCj_money().doubleValue()).sum();//100
                                Integer real_hand = no_cj.stream().mapToInt(Chengjiao::getHand).sum();//100
                                changeOrder(all_hand,new BigDecimal(cj_money),order,0,real_hand,no_cj);
                            }
                            if(chengjiaos.size()>0 && no_cj.size() ==0 && order.getStock_status() == 5){
                                Map map = new HashMap();
                                map.put("id",order.getId());
                                map.put("state",2);
                                orderServiceImpl.updateOrderState(map);
                            }
                        });
                    }

                }
        );

    }
    public void changeOrder(Integer chengjiao_num,BigDecimal chengjiao_money,nettyOrder order,Integer flag,Integer real_hand,List noChenJiao){

        if(order.getTrade_direction() == 1){
            //买的情况下，设置父订单
            if(order.getPid() == 0){
                Map pa = new HashMap<>();
                pa.put("heyue_id",order.getMember_heyue_id());
                pa.put("member_id",order.getMember_id());
                pa.put("stock_code",order.getStock_code());
                nettyOrder parentOrder = orderServiceImpl.findParentOrder(pa);
                if(parentOrder != null){
                    if(parentOrder.getId() != order.getId()){
                        order.setPid(parentOrder.getId());
                        Map dp = new HashMap<>();
                        dp.put("id",order.getId());
                        dp.put("pid",order.getPid());
                        orderServiceImpl.updateOrderPid(dp);
                    }
                }
            }
            //扣除手续费

        }
        BigDecimal last_price;
        last_price = chengjiao_money.divide(new BigDecimal(real_hand),3,RoundingMode.HALF_UP);
        //买入增加手数,卖减少冻结手数

        Integer stock_status = chengjiao_num < order.getWeituo_hand()?1:2; //部成部撤继续委托
        if(order.getStock_status() == 5){
            stock_status = 2;
        }
        Integer buy_hand = chengjiao_num;
        orderServiceImpl.updateChengJiaoStatus(noChenJiao);
        BigDecimal buy_price = last_price;

        Map map = new HashMap<>();
        map.put("stock_status",stock_status);
        map.put("id",order.getId());
        map.put("buy_hand",chengjiao_num);
        map.put("pid",order.getPid());

        Map memberFengKong = memberService.findMemberFengKong(order.getMember_id());
        BigDecimal yj_rate = (BigDecimal)memberFengKong.get("yj_rate");
        BigDecimal sx_fee =  get_sx_fee(new BigDecimal(real_hand),buy_price,yj_rate);
        BigDecimal yh_fee = get_yh_fee(new BigDecimal(real_hand),buy_price);
        map.put("buy_price",buy_price);
        map.put("sx_fee",sx_fee);
        map.put("yh_fee",order.getTrade_direction() == 2?yh_fee:0);
        map.put("up_time",1);
        map.put("part_suc",0);
        orderServiceImpl.updateBuyOrderState(map);
        BigDecimal real_trade_money = chengjiao_money;

        if(order.getTrade_direction() == 1 && flag == 0){
            Map map2 = new HashMap();
            map2.put("member_heyue_id",order.getMember_heyue_id());
            map2.put("amount",sx_fee);
            memberHeYueApply.decreaseHeYueMoney(map2);
            com.stock.models.MemberHeYueApply memberHeYue_1 = this.memberHeYueApply.selectHeyueById(order.getMember_heyue_id());
            Map bill2 = new HashMap();
            bill2.put("member_id",order.getMember_id());
            bill2.put("link_id",order.getId());
            bill2.put("mark","用户"+order.getMember_id()+"买入股票扣除手续费"+sx_fee.setScale(2,RoundingMode.HALF_UP).toString()+"元");
            bill2.put("amount",sx_fee);
            bill2.put("after_amount",memberHeYue_1.getTotal_capital());
            bill2.put("type", BillCode.STOCK_BUY_FEE.getCode());
            memberService.addBill(bill2);
            BigDecimal subtract = order.getFreeze_money().subtract(real_trade_money);//-5
            if(subtract.compareTo(BigDecimal.ZERO) != 0){
                Map m1 = new HashMap();
                m1.put("member_heyue_id",order.getMember_heyue_id());
                if(subtract.compareTo(BigDecimal.ZERO) <0){
                    subtract = subtract.abs();
                    m1.put("amount",subtract);
                    memberHeYueApply.decreaseHeYueMoney(m1);
                    com.stock.models.MemberHeYueApply memberHeYue_ = this.memberHeYueApply.selectHeyueById(order.getMember_heyue_id());
                    Map bill = new HashMap();
                    bill.put("member_id",order.getMember_id());
                    bill.put("link_id",order.getId());
                    bill.put("mark","用户"+order.getMember_id()+"买入股票补充差价"+subtract.setScale(2,RoundingMode.HALF_UP).toString()+"元");
                    bill.put("amount",subtract);
                    bill.put("after_amount",memberHeYue_.getTotal_capital());
                    bill.put("type", BillCode.STOCK_TRADE_DIFF.getCode());
                    memberService.addBill(bill);
                }else{
//                        m1.put("amount",subtract);
//                        memberHeYueApply.increaseHeYueMoney(m1);
                }
            }
        }
        nettyOrder p_order = orderServiceImpl.findOrderById(order.getPid());
        if(order.getPid() != 0 && p_order.getStock_status() != 1 ){//加仓或减仓
            Integer parent_stock_status = 2;
            BigDecimal p_order_money = new BigDecimal(p_order.getBuy_hand()).multiply(new BigDecimal(p_order.getBuy_price()));
            BigDecimal cal_order_money;
            BigDecimal sum_order_hand;
            BigDecimal sum_order_money;
            if(order.getTrade_direction() == 1){ //如果是买
                sum_order_hand = new BigDecimal(p_order.getBuy_hand()).add(new BigDecimal(real_hand));

                sum_order_money = p_order_money.add(chengjiao_money).add(get_sx_fee(new BigDecimal(real_hand),buy_price,yj_rate));
                cal_order_money = p_order_money.add(chengjiao_money);
            }else{
                sum_order_hand = new BigDecimal(p_order.getBuy_hand()).subtract(new BigDecimal(real_hand));
                sum_order_money = p_order_money.subtract(chengjiao_money).add(get_yh_fee(new BigDecimal(real_hand),buy_price)).add(get_sx_fee(new BigDecimal(real_hand),buy_price,yj_rate));
                cal_order_money = p_order_money.subtract(chengjiao_money);
                if(real_hand >= p_order.getBuy_hand()){  //2700-900
                    parent_stock_status = 4;
                }
                //给合约加钱
                //BigDecimal back_money = chengjiao_money.subtract(get_yh_fee(new BigDecimal(real_hand),buy_price)).subtract(get_sx_fee(new BigDecimal(real_hand),buy_price));
                BigDecimal back_money = chengjiao_money.subtract(get_yh_fee(new BigDecimal(real_hand),buy_price)).subtract(get_sx_fee(new BigDecimal(real_hand),buy_price,yj_rate));

                Map data = new HashMap();
                data.put("amount",back_money);
                data.put("member_heyue_id",order.getMember_heyue_id());
                memberHeYueApply.increaseHeYueMoney(data);
                com.stock.models.MemberHeYueApply memberHeYue = this.memberHeYueApply.selectHeyueById(order.getMember_heyue_id());
                Map bill = new HashMap();
                bill.put("member_id",order.getMember_id());
                bill.put("link_id",order.getId());
                bill.put("mark","用户"+order.getMember_id()+"卖出股票合约账户增加"+back_money.setScale(2,RoundingMode.HALF_UP).toString()+"元");
                bill.put("amount",back_money);
                bill.put("after_amount",memberHeYue.getTotal_capital());
                bill.put("type", BillCode.SELL_STOCK.getCode());
                memberService.addBill(bill);
                Map unfreeze_map = new HashMap();
                unfreeze_map.put("id",p_order.getId());
                unfreeze_map.put("hand",real_hand);//解冻手数
                unfreeze_map.put("buy_hand",real_hand);
                orderServiceImpl.unfreeze_hand(unfreeze_map);
                orderServiceImpl.decreaseHand(unfreeze_map);
                Map ddw = new HashMap();
                ddw.put("id",p_order.getId());
                ddw.put("hand",real_hand);
                orderServiceImpl.addSellHand(ddw);
            }
            sum_order_money = sum_order_money.add(new BigDecimal((0-p_order.getProfit())));

            //buy_price = parent_stock_status==4? new BigDecimal(0):sum_order_money.divide(sum_order_hand,RoundingMode.HALF_UP).setScale(3,RoundingMode.HALF_UP);
            buy_price = parent_stock_status==4? new BigDecimal(0):cal_order_money.divide(sum_order_hand,3,RoundingMode.HALF_UP);
            BigDecimal diff_price = last_price.subtract(new BigDecimal(p_order.getBuy_price())).setScale(3,RoundingMode.HALF_UP);
            BigDecimal money = diff_price.multiply(new BigDecimal(real_hand));
            if(order.getTrade_direction() == 2){
                if(parent_stock_status == 4){
                    //给订单盈利价钱
                    Map l = new HashMap();
                    l.put("profit",money);
                    l.put("id",p_order.getId());
                    l.put("sell_price",last_price);
                    orderServiceImpl.updateOrderProfit(l);
                }
            }
            Map p_map = new HashMap<>();
            p_map.put("stock_status",parent_stock_status);
            p_map.put("id",p_order.getId());
            p_map.put("buy_hand",sum_order_hand);
            p_map.put("buy_price",buy_price);
            p_map.put("pid",p_order.getPid());
            p_map.put("up_time",0);
            orderServiceImpl.updateBuyOrderState(p_map);
        }
    }


    //处理撤销和部撤
    //@Scheduled(cron = "0 10 15 * * ?")
    @Scheduled(cron = "0/15 * * * * ?")
    @Transactional(rollbackFor=Exception.class)
    public void checkCancel(){
        List<broker> all = brokerService.getAll();
        all.forEach(p->{
            Map weituo = TdxUtil.queryData("2",p.getAccount(),p.getPassword(),p.getTx_password(),p.getIp(),p.getPort());
            List<Map> weituo_data = (List<Map>)weituo.get("data");
            if(weituo_data != null){
                Map param = new HashMap();
                param.put("stock_status",1);
                param.put("broker_id",p.getId());
                List weituo_order = orderServiceImpl.findOrderByStateByBrokerId(param);
                if (weituo_order.size()>0){
                    weituo_order.forEach(e->{
                        nettyOrder order = (nettyOrder)e;
                        weituo_data.forEach(s->{

                            String d = "";
                            if(s.containsKey("委托编号")){
                                d = "委托编号";
                            }
                            if(s.containsKey("合同编号")){
                                d = "合同编号";
                            }
                            if(s.get(d).equals(order.getContract_no())){
                                int flag = 0;
                                String cc = "";
                                if(s.containsKey("撤消数量")){
                                    cc = "撤消数量";
                                }
                                if(s.containsKey("撤单数量")){
                                    cc = "撤单数量";
                                }
                                if( Integer.parseInt((String) s.get(cc)) >0 &&  Double.valueOf((String)s.get(cc)).intValue() !=   Double.valueOf((String)s.get("委托数量")).intValue()){
                                    Integer chengjiao_num =  Double.valueOf((String) s.get("成交数量")).intValue();
                                    BigDecimal weituo_num = new BigDecimal((String) s.get("委托数量"));
                                    BigDecimal bucheng_can_weituo = weituo_num.subtract(new BigDecimal(chengjiao_num));
                                    if(order.getTrade_direction() == 1){  //买退钱
                                        BigDecimal bank_money = bucheng_can_weituo.multiply(new BigDecimal((String) s.get("委托价格")));
                                        if(bank_money.doubleValue()>0){
                                            Map data = new HashMap();
                                            data.put("amount",bank_money);
                                            data.put("member_heyue_id",order.getMember_heyue_id());
                                            memberHeYueApply.increaseHeYueMoney(data);
                                            com.stock.models.MemberHeYueApply memberHeYue = this.memberHeYueApply.selectHeyueById(order.getMember_heyue_id());
                                            Map bill = new HashMap();
                                            bill.put("member_id",order.getMember_id());
                                            bill.put("link_id",order.getId());
                                            String str = "订单部分撤回增加";
                                            bill.put("mark","用户"+order.getMember_id()+str+bank_money.setScale(2,RoundingMode.HALF_UP).toString()+"元");
                                            bill.put("amount",bank_money);
                                            bill.put("after_amount",memberHeYue.getTotal_capital());
                                            bill.put("type", BillCode.CANCLE_ORDER.getCode());
                                            memberService.addBill(bill);
                                            Map map1 = new HashMap<>();
                                            map1.put("id",order.getId());
                                            orderServiceImpl.updateOrderToPart(map1);
                                        }
                                    }else{
                                        Map map1 = new HashMap<>();
                                        map1.put("id",order.getId());
                                        orderServiceImpl.updateOrderToPart(map1);
                                        Map unfreeze_map = new HashMap();
                                        unfreeze_map.put("id",order.getPid());
                                        unfreeze_map.put("hand",bucheng_can_weituo);
                                        orderServiceImpl.unfreeze_hand(unfreeze_map);
                                    }
                                }else if (Double.valueOf((String)s.get(cc)).intValue()>0 && Double.valueOf((String)s.get(cc)).intValue() ==   Double.valueOf((String)s.get("委托数量")).intValue()){

                                    updateOrder(order, new BigDecimal((String)s.get("委托价格")),1,new BigDecimal((String) s.get("委托数量")) );

                                }


                            }
                        });
                    });
                }
            }
        });
    }



    public void updateOrder(nettyOrder order,BigDecimal weituo_price,int flag,BigDecimal weituo_num){
        if(order.getStock_status() == 1){
            //等于委托中才让撤单
            int weituo_hand = order.getWeituo_hand();
            int buy_hand = order.getBuy_hand();
            if(order.getTrade_direction() == 1){
                Map map = new HashMap<>();
                map.put("id",order.getId());

                //给用户合约价钱
                BigDecimal money = new BigDecimal(0);
                if(weituo_hand == buy_hand){
                    money = new BigDecimal(order.getBuy_hand()).multiply(weituo_price);
                    orderServiceImpl.cancelOrder(map);
                }
                if(weituo_hand > buy_hand){
                    money = new BigDecimal(weituo_hand-buy_hand).multiply(weituo_price);
                    map.put("state",2);
                    orderServiceImpl.updateOrderState(map);
                }
                Map data = new HashMap();
                data.put("amount",money);
                data.put("member_heyue_id",order.getMember_heyue_id());
                memberHeYueApply.increaseHeYueMoney(data);
                com.stock.models.MemberHeYueApply memberHeYue = this.memberHeYueApply.selectHeyueById(order.getMember_heyue_id());

                Map bill = new HashMap();
                bill.put("member_id",order.getMember_id());
                bill.put("link_id",order.getId());
                String str = "";
                if(flag ==1){
                    str = "订单撤回增加";
                }else{
                    str = "订单已作废撤回增加";
                }
                bill.put("mark","用户"+order.getMember_id()+str+money.setScale(2,RoundingMode.HALF_UP).toString()+"元");
                bill.put("amount",money);
                bill.put("after_amount",memberHeYue.getTotal_capital());
                bill.put("type", BillCode.CANCLE_ORDER.getCode());
                memberService.addBill(bill);
            }else{
                //
                Map map = new HashMap<>();
                map.put("id",order.getId());
                int hand = 0;
                if(weituo_hand == buy_hand){
                    hand =weituo_hand;
                    orderServiceImpl.cancelOrder(map);
                }
                if(weituo_hand>buy_hand){
                    hand = weituo_hand-buy_hand;
                    map.put("state",2);
                    orderServiceImpl.updateOrderState(map);
                }


                Map unfreeze_map = new HashMap();
                unfreeze_map.put("id",order.getPid());
                unfreeze_map.put("hand",hand);
                orderServiceImpl.unfreeze_hand(unfreeze_map);

            }
        }
    }



    @Scheduled(cron = "0/14 * 8-23 * * ?")
    @Transactional(rollbackFor=Exception.class)
    public void updateChenjiao(){
        List<broker> all = brokerService.getAll();
        all.forEach(p->{
                    Map chengjiao = TdxUtil.queryData("3",p.getAccount(),p.getPassword(),p.getTx_password(),p.getIp(),p.getPort());
                    if(chengjiao != null){
                        List<Map> chenjiao_data = (List<Map>)chengjiao.get("data");
                        if(chenjiao_data != null){
                            List<String> cc = chenjiao_data.stream().map(s->(String)s.get("成交编号")).collect(Collectors.toList());
                            List<Chengjiao> today_cj = orderServiceImpl.findTodayChengjiao();
                            List<String> dd = today_cj.stream().map(Chengjiao::getCj_no).collect(Collectors.toList());
                            cc.removeAll(dd);
                            List<Chengjiao> ee = new ArrayList();
                            if(cc.size()>0){
                                cc.forEach(a->{
                                    chenjiao_data.forEach(s->{
                                        if(s.get("成交编号").equals((String)a)){
                                            String ss = "成交金额";
                                            if(s.containsKey("发生金额")){
                                                ss = "发生金额";
                                            }
                                            Chengjiao chengjiao1 = new Chengjiao();
                                            chengjiao1.setCj_money(new BigDecimal((String) s.get(ss)));
                                            chengjiao1.setHand(Double.valueOf((String) s.get("成交数量")).intValue());
                                            chengjiao1.setCj_no((String) s.get("成交编号"));
                                            String d = "";
                                            if(s.containsKey("委托编号")){
                                                d = "委托编号";
                                            }
                                            if(s.containsKey("合同编号")){
                                                d = "合同编号";
                                            }
                                            chengjiao1.setContract_no((String) s.get(d));
                                            String pp = "";
                                            if(s.containsKey("成交均价")){
                                                pp = "成交均价";
                                            }
                                            if(s.containsKey("成交价格")){
                                                pp = "成交价格";
                                            }
                                            chengjiao1.setCj_price(new BigDecimal((String) s.get(pp)));
                                            if(s.get("成交编号") == "" || s.get("成交编号") == null){
                                                return;
                                            }
                                            if(Double.parseDouble((String) s.get(ss))<=0){
                                                return;
                                            }
                                            chengjiao1.setStock_code((String) s.get("证券代码"));
                                            ee.add(chengjiao1);
                                        }
                                    });
                                });
                            }
                            if(ee.size()>0){
                                orderServiceImpl.addChenJiaoList(ee);
                            }
                        }

                    }

                }
        );

    }




    @Scheduled(cron = "0/10 * * * * ?")
    @Transactional(rollbackFor=Exception.class)
    public void updateBrokerMoney(){
        List<broker> all = brokerService.getAll();
        all.forEach(s->{
            Map map = TdxUtil.queryData("0",s.getAccount(),s.getPassword(),s.getTx_password(),s.getIp(),s.getPort());
            List d = (List) map.get("data");
            Map data = (Map) d.get(0);
            if(data != null){
                BigDecimal amount = new BigDecimal((String) data.get("可用资金"));
                BigDecimal total_amount = new BigDecimal((String) data.get("总资产"));
                Map map_ = new HashMap();
                map_.put("id",s.getId());
                map_.put("amount",amount);
                map_.put("total_amount",total_amount);
                brokerService.updateMoney(map_);
            }

        });
    }


    @Scheduled(cron = "0 0 22 * * ?")
    //@Scheduled(cron = "0/5 * * * * ?")
    @Transactional(rollbackFor=Exception.class)
    //检测每天委托失败的订单
    public void checkFailOrder(){
        List<broker> all = brokerService.getAll();
        all.forEach(p->{
            Map weituo = TdxUtil.queryData("2",p.getAccount(),p.getPassword(),p.getTx_password(),p.getIp(),p.getPort());
            List<Map> weituo_data = (List<Map>)weituo.get("data");
            if(weituo_data != null){
                Map param = new HashMap();
                param.put("stock_status",1);
                param.put("broker_id",p.getId());
                SimpleDateFormat formatter_  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c1 = Calendar.getInstance();
                c1.set(Calendar.HOUR_OF_DAY,15);
                c1.set(Calendar.MINUTE,0);
                c1.set(Calendar.SECOND,0);
                String format = formatter_.format(c1.getTime());
                param.put("time",format);
                param.put("ping_way",0);
                List weituo_order = orderServiceImpl.findOrderByStateByBrokerId(param);
                if(weituo_order != null){
                    if (weituo_order.size()>0){
                        weituo_order.forEach(e->{
                            nettyOrder order = (nettyOrder)e;
                            weituo_data.forEach(s->{
                                String a = "";
                                if(s.containsKey("委托编号")){
                                    a = "委托编号";
                                }
                                if(s.containsKey("合同编号")){
                                    a = "合同编号";
                                }
                                if(s.get(a).equals(order.getContract_no())){
                                    updateOrder(order, new BigDecimal((String)s.get("委托价格")),1,new BigDecimal((String) s.get("委托数量")) );
                                }
                            });
                        });
                    }
                }
            }
        });
    }
}
