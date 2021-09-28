package com.mapper.frontend;

import com.stock.models.Chengjiao;
import com.stock.models.frontend.Order;
import com.stock.models.frontend.nettyOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    int makerOrder(Order order);
    List findOrderByCaseByPage(Map map);
    List findOrderByState(Map map);
    List findHoldOrder(Map map);
    int updateBuyOrderState(Map map);
    nettyOrder findOrderById(Integer id);
    int cancelOrder(Map map);
    int apply_cancel(Integer id);
    int updateOrderSystem(Integer id);
    Integer findWeiTuoCount(Integer heyue_id);
    Integer findTodayHoldCount(Map map);

    int sellStock(nettyOrder order_);
    nettyOrder findParentOrder(Map map);
    int freezeHand(Map source);

    int unfreeze_hand(Map unfreeze_map);
    int decreaseHand(Map map);

    int closeOrder(int id);

    List findAllOrderByCase(Map m);

    Integer findActiveOrder(Integer member_heyue_id);

    List<nettyOrder> findActiveOrderMoney(int member_heyue_id);

    int updateOrderProfit(Map l);

    List<?> getOrderListByPage(Map<String, Object> map);

    Map findAllFee(Map<String, Object> map);

    int updateOrderPid(Map data);

    List<nettyOrder> findOrderByPid(int id);

    int addSellHand(Map ddw);

    List findCanWeiTuo(int broker_id);

    nettyOrder findOrderByCode(Map er);

    List findOrderByStateByBrokerId(Map param);

    int setOrderBroker(Map map);

    int updateOrderState(Map upo);

    int decreaseWeiTuoHand(Map weituo);

    void addPartLog(Map partLog);

    int findPartLog(Map part);

    int findPartOrder(Map part);

    Chengjiao findBeforeChenJiao(String contract_no);

    void addChenJiao(Map cj_map);

    List<Chengjiao> findTodayChengjiao();

    void addChenJiaoList(@Param(value = "ee")List<Chengjiao> ee);

    List<Chengjiao> NoChengjiaoByContractNo(String contract_no);

    void updateChengJiaoStatus(@Param(value = "noChenJiao")List noChenJiao);

    void updateOrderToPart(Map map1);

    void updateMemberOrder(Map<String, Object> map);

    Integer findStockHand(Map map);
}
