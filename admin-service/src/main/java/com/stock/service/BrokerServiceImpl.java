package com.stock.service;

import com.mapper.BrokerMapper;
import com.stock.models.broker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class BrokerServiceImpl implements BrokerService {
    @Autowired
    BrokerMapper brokerMapper;
    @Override
    public List<broker> getAll() {
        return brokerMapper.getAll();
    }

    @Override
    public int updateMoney(Map map_) {
        return brokerMapper.updateMoney(map_);
    }

    @Override
    public int addBroker(broker broker) {
        return brokerMapper.addBroker(broker);
    }

    @Override
    public int updateBroker(broker broker) {
        return brokerMapper.updateBroker(broker);
    }

    @Override
    public int updateStatus(Map map) {
        return brokerMapper.updateStatus(map);
    }

    @Override
    public List<broker> getAllValid() {
        return brokerMapper.getAllValid();
    }

    @Override
    public broker findBrokerById(int broker_id) {
        return brokerMapper.findBrokerById(broker_id);
    }

    @Override
    public List<Map> getChuQuan() {
        return brokerMapper.getChuQuan();
    }

    @Override
    public void updateChuQuan(Map<String, Object> map) {
        brokerMapper.updateChuQuan(map);
    }

    @Override
    public void addChuQuan(Map<String, Object> map) {
        brokerMapper.addChuQuan(map);
    }

    @Override
    public void deleteChuQuan(Map<String, Object> map) {
        brokerMapper.deleteChuQuan(map);
    }

    @Override
    public BigDecimal findPriceByCode(String stock_code) {
        return brokerMapper.findPriceByCode(stock_code);
    }
}
