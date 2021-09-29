package com.stock.service.ServiceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mapper.frontend.OrderMapper;
import com.stock.models.Chengjiao;
import com.stock.models.broker;
import com.stock.models.frontend.Order;
import com.stock.models.frontend.nettyOrder;
import com.stock.service.BrokerService;
import com.stock.service.OrderService;
import com.stock.service.StockDataServiceAbstract;
import com.util.BillCode;
import com.util.TdxUtil;
import com.util.XhbUtil;
import event.SellOrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import event.makeOrderEvent;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    private RedisTemplate redisTemplate_;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Qualifier("sina")
    @Autowired
    StockDataServiceAbstract SinaStockServiceImpl;

    @Autowired
    BrokerService brokerService;

    @Override
    public int unfreeze_hand(Map unfreeze_map) {
       return orderMapper.unfreeze_hand(unfreeze_map);
    }

    @Override
    public int closeOrder(int id) {
        return orderMapper.closeOrder(id);
    }

    @Override
    public List findAllOrderByCase(Map m) {
        return orderMapper.findAllOrderByCase(m);
    }

    @Override
    public Integer findActiveOrder(Integer id) {
        return orderMapper.findActiveOrder(id);
    }

    @Override
    public List<nettyOrder> findActiveOrderMoney(int id) {
        return orderMapper.findActiveOrderMoney(id);
    }

    @Override
    public int decreaseHand(Map map) {
        return orderMapper.decreaseHand(map);
    }

    @Override
    public int updateOrderProfit(Map l) {
        return orderMapper.updateOrderProfit(l);
    }

    @Override
    public List<?> getOrderList(Map<String, Object> map) {
        return orderMapper.getOrderListByPage(map);
    }

    @Override
    public Map findAllFee(Map<String, Object> map) {
        return orderMapper.findAllFee(map);
    }

    @Override
    public int updateOrderPid(Map data) {
        return orderMapper.updateOrderPid(data);
    }

    @Override
    public List<nettyOrder> findOrderByPid(int id) {
        return orderMapper.findOrderByPid(id);
    }

    @Override
    public int addSellHand(Map ddw) {
        return orderMapper.addSellHand(ddw);
    }

    @Override
    public List findCanWeiTuo(int broker_id) {
        return orderMapper.findCanWeiTuo(broker_id);
    }

    @Override
    public List findOrderByStateByBrokerId(Map param) {
        return orderMapper.findOrderByStateByBrokerId(param);
    }

    @Override
    public int setOrderBroker(Map map) {
        return orderMapper.setOrderBroker(map);
    }

    @Override
    public int updateOrderState(Map upo) {
        return orderMapper.updateOrderState(upo);
    }

    @Override
    public int decreaseWeiTuoHand(Map weituo) {
        return orderMapper.decreaseWeiTuoHand(weituo);
    }

    @Override
    public void addPartLog(Map partLog) {
        orderMapper.addPartLog(partLog);
    }

    @Override
    public int findPartLog(Map part) {
        return orderMapper.findPartLog(part);
    }

    @Override
    public Chengjiao findLastChenJiao(String contract_no) {
        return orderMapper.findBeforeChenJiao(contract_no);
    }

    @Override
    public void addChenJiao(Map cj_map) {
        orderMapper.addChenJiao(cj_map);
    }

    @Override
    public List<Chengjiao> findTodayChengjiao() {
        return orderMapper.findTodayChengjiao();
    }

    @Override
    public void addChenJiaoList(List<Chengjiao> ee) {
        orderMapper.addChenJiaoList(ee);
    }

    @Override
    public List<Chengjiao> NoChengjiaoByContractNo(String contract_no) {
        return orderMapper.NoChengjiaoByContractNo(contract_no);
    }

    @Override
    public void updateChengJiaoStatus(List noChenJiao) {
        orderMapper.updateChengJiaoStatus(noChenJiao);
    }

    @Override
    public void updateOrderToPart(Map map1) {
        orderMapper.updateOrderToPart(map1);
    }

    @Override
    public void updateMemberOrder(Map<String, Object> map) {
        orderMapper.updateMemberOrder(map);
    }

    @Override
    public Integer findStockHand(Map map) {
        return orderMapper.findStockHand(map);
    }

    public int findPartOrder(Map part){
        return orderMapper.findPartOrder(part);
    }

    @Override
    public int makerOrder(Order order) {

        Map part = new HashMap();
        part.put("member_id",order.getMember());
        part.put("stock_code",order.getStock_code());
        int partOrder = findPartOrder(part);
        if (partOrder>0){
            throw new RuntimeException("有股票在部成中，请等待成交或者撤销");
        }

        if(order.getTrade_direction() == 1){//如果是买并且第一次买这只股票必须选择钱多的券商账户
            Map er = new HashMap();
            er.put("member_id",order.getMember_id());
            er.put("stock_code",order.getStock_code());
           nettyOrder os = orderMapper.findOrderByCode(er);
           if(os == null){//说明第一次买这个股票，就选择钱多的账
               List<broker> allValid = brokerService.getAllValid();
               broker broker = null;
               double dd = order.getBuy_price()*order.getBuy_hand();
               for(int i =0;i<allValid.size();i++){

                   if(allValid.get(i).getAmount().doubleValue()>dd){
                       broker = allValid.get(i);
                       break;
                   }
               }
               if(broker == null){
                   throw new RuntimeException("券商余额不足");
               }
               order.setBroker_id(broker.getId());

           }else{
               order.setBroker_id(os.getBroker_id());
           }
        }else{
            nettyOrder pod = orderMapper.findOrderById(order.getPid());
            broker brokerById = brokerService.findBrokerById(pod.getBroker_id());

            order.setBroker_id(brokerById.getId());
        }

        broker member_broker = brokerService.findBrokerById(order.getBroker_id());

        Map res = XhbUtil.sendOrder(order.getStock_code(), order.getBuy_hand(), order.getBuy_price(), order.getTrade_direction(),member_broker.getAccount(),member_broker.getPassword(),member_broker.getIp(),member_broker.getPort());
        System.out.println(res.get("code"));
        if(!res.get("code").equals("0")){
            throw new RuntimeException("委托失败"+"券商"+res.get("msg"));
        }
        return  doorder(res,order);
    }

    @Transactional(rollbackFor = Exception.class)
    public int doorder(Map res,Order order){
        List data = (List) res.get("data");
        Map order_data = (Map) data.get(0);
        order.setContract_no((String) order_data.get("entrust_no"));
        BigDecimal hand = BigDecimal.valueOf(order.getBuy_hand());
        BigDecimal price = BigDecimal.valueOf(order.getBuy_price());
        int ret ;
        if(order.getTrade_direction() == 1){
            order.setFreeze_money(hand.multiply(price));
        }else{
            order.setFreeze_money(new BigDecimal(0));
        }
        ret = orderMapper.makerOrder(order);

        Map map_ = new HashMap();
        map_.put("member_id",order.getMember_id());
        map_.put("amount",hand.multiply(price));
        map_.put("type", BillCode.DO_ORDER.getCode());
        map_.put("buy_hand", order.getBuy_hand());
        map_.put("pid", order.getPid());

        String mark = "";
        if(order.getTrade_direction() == 1){
            mark = "用户"+order.getMember_id()+"下单买入"+map_.get("amount").toString()+"元";
        }else{
            mark = "用户"+order.getMember_id()+"下单卖出"+map_.get("amount").toString()+"元";
        }
        map_.put("mark", mark);
        map_.put("link_id", order.getId());
        map_.put("member_heyue_id", order.getMember_heyue_id());
        if(order.getPid() == 0){
            redisTemplate_.opsForHash().put(Integer.toString(order.getMember_heyue_id()),Integer.toString(order.getId()), JSON.toJSONString(order));
        }
        applicationEventPublisher.publishEvent(order.getTrade_direction()==1?new makeOrderEvent(map_):new SellOrderEvent(map_));
        return ret;
    }


    @Override
    public List findOrderByCase(Map map) {
        return orderMapper.findOrderByCaseByPage(map);
    }

    @Override
    public List findOrderByState(Map map) {
        return orderMapper.findOrderByState(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBuyOrderState(Map map) {
        return orderMapper.updateBuyOrderState(map);
    }

    @Override
    public nettyOrder findOrderById(Integer id) {
        return orderMapper.findOrderById(id);
    }

    @Override
    public int cancelOrder(Map map) {
        return orderMapper.cancelOrder(map);
    }

    @Override
    public List findHoldOrder(Map map) {
        return orderMapper.findHoldOrder(map);
    }

    @Override
    public int apply_cancel(Integer id) {
        nettyOrder order = findOrderById(id);
        broker member_broker = brokerService.findBrokerById(order.getBroker_id());
        JSONObject jsonObject = TdxUtil.cancelOrder_(order.getStock_code(), order.getContract_no(),member_broker.getAccount(),member_broker.getPassword(),member_broker.getIp(),member_broker.getPort());
       String str = (String)jsonObject.get("msg");
       if(!jsonObject.get("code").equals("0")){
                throw new RuntimeException((String) jsonObject.get("msg"));
       }
       nettyOrder order_ = findOrderById(id);
        if(order_.getStock_status() != 1){
            throw new RuntimeException("非委托中的单不能撤销");
        }
            return orderMapper.apply_cancel(id);
    }

    @Override
    public int updateOrderSystem(Integer id) {
        return orderMapper.updateOrderSystem(id);
    }

    @Override
    public Integer findWeiTuoCount(Integer heyue_id) {
        return orderMapper.findWeiTuoCount(heyue_id);
    }

    @Override
    public void setOrder(nettyOrder order) {
        BigDecimal stockPrice = SinaStockServiceImpl.setDataSource(order.getStock_code()).getStockPrice();
        int max=60;
        int min=1;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        //stockPrice = new BigDecimal(s);
        order.setNow_price(stockPrice);
    }

    @Override
    public Integer findTodayHoldCount(Map map) {
        return orderMapper.findTodayHoldCount(map);
    }

    @Override
    public int sellStock(nettyOrder order) {
        broker member_broker = brokerService.findBrokerById(order.getBroker_id());
       Map res = TdxUtil.sendOrder(order.getStock_code(), order.getBuy_hand(), order.getBuy_price(), 1,member_broker.getAccount(),member_broker.getPassword(),member_broker.getIp(),member_broker.getPort());
        if((Integer) res.get("code") != 0){
            throw new RuntimeException("委托失败"+"券商"+res.get("msg"));
        }
        List data = (List) res.get("data");
        Map order_data = (Map) data.get(0);
        order.setContract_no((String) order_data.get("orderID"));
        return orderMapper.sellStock(order);
    }

    @Override
    public nettyOrder findParentOrder(Map map) {
        return orderMapper.findParentOrder(map);
    }

    @Override
    public int freezeHand(Map source) {
        return orderMapper.freezeHand(source);
    }
}
