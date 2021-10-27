package com.stock.service;

import com.stock.models.Chengjiao;
import com.stock.models.frontend.Order;
import com.stock.models.frontend.nettyOrder;

import java.util.List;
import java.util.Map;

public interface OrderService {
    int makerOrder(Order order);
    List findOrderByCase(Map map);
    List findOrderByState(Map map);
    int updateBuyOrderState(Map map);
    nettyOrder findOrderById(Integer id);
    int cancelOrder(Map map);
    List findHoldOrder(Map map);
    int apply_cancel(Integer id);
    int updateOrderSystem(Integer id);
    Integer findWeiTuoCount(Integer heyue_id);
    void setOrder(nettyOrder order);

    Integer findTodayHoldCount(Map map);

    int sellStock(nettyOrder order_);
    nettyOrder findParentOrder(Map map);

    int freezeHand(Map source);
    int unfreeze_hand(Map unfreeze_map);

    int closeOrder(int id);

    List findAllOrderByCase(Map m);

    Integer findActiveOrder(Integer id);

    List<nettyOrder> findActiveOrderMoney(int id);
    int decreaseHand(Map map);

    int updateOrderProfit(Map l);

    List<?> getOrderList(Map<String, Object> map);

    Map findAllFee(Map<String, Object> map);


    int updateOrderPid(Map data);

    List<nettyOrder> findOrderByPid(int id);

    int addSellHand(Map ddw);

    List findCanWeiTuo(int broker_id);

    List findOrderByStateByBrokerId(Map param);
    int setOrderBroker(Map map);

    int updateOrderState(Map upo);

    int decreaseWeiTuoHand(Map weituo);

    void addPartLog(Map partLog);

    int findPartLog(Map part);

    Chengjiao findLastChenJiao(String contract_no);

    void addChenJiao(Map cj_map);

    List<Chengjiao> findTodayChengjiao();

    void addChenJiaoList(List<Chengjiao> ee);

    List<Chengjiao> NoChengjiaoByContractNo(Map map);

    void updateChengJiaoStatus(List noChenJiao);

    void updateOrderToPart(Map map1);

    void updateMemberOrder(Map<String, Object> map);

    Integer findStockHand(Map map);
}


