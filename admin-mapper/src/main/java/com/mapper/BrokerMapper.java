package com.mapper;



import com.stock.models.broker;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface BrokerMapper {
    List<broker> getAll();

    int updateMoney(Map map_);

    int addBroker(broker broker);

    int updateBroker(broker broker);

    int updateStatus(Map map);

    List<broker> getAllValid();

    broker findBrokerById(int broker_id);

    List<Map> getChuQuan();

    void updateChuQuan(Map<String, Object> map);

    void addChuQuan(Map<String, Object> map);

    void deleteChuQuan(Map<String, Object> map);

    BigDecimal findPriceByCode(String stock_code);
}
