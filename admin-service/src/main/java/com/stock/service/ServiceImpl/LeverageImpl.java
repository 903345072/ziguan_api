package com.stock.service.ServiceImpl;


import com.mapper.HeYueMapper;
import com.mapper.LeverageMapper;
import com.stock.models.HeYue;
import com.stock.models.Leverage;
import com.stock.service.HeYueService;
import com.stock.service.LeverageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("LeverageImpl")
public class LeverageImpl implements LeverageService {
    @Autowired
    LeverageMapper leverageMapper;
    @Override
    public List findLeverage(Map map) {
        return leverageMapper.findLeverageByPage(map);
    }

    @Override
    public int findLeverageCount(Map map) {
        return leverageMapper.findLeverageCount(map);
    }

    @Override
    public int insertOneLeverage(Leverage leverage) {
        return leverageMapper.insertOneLeverage(leverage);
    }

    @Override
    public int updateOneLeverage(Leverage leverage) {
        return leverageMapper.updateOneLeverage(leverage);
    }

    @Override
    public int updateLeverageStaus(Map map) {
        return leverageMapper.updateLeverageStaus(map);
    }

    @Override
    public Map findLeverageNameById(Integer id) {
        return leverageMapper.findLeverageNameById(id);
    }

    @Override
    public List<Map> findValidLeverageIdList() {
        return leverageMapper.findValidLeverageIdList();
    }
}
