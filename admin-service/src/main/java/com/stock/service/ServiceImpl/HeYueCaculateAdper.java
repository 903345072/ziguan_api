package com.stock.service.ServiceImpl;

import com.mapper.HeYueMapper;
import com.stock.service.LeverageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service(value = "HeYueCaculateAdper")
class HeYueCaculateAdper extends HeYueCaculateImpl implements HeYueCaculateAdpterInterface{

    @Override
    public double getLeverageName(int leverage_id) {
        Map map =  LeverageImpl.findLeverageNameById(leverage_id);
        return (double)map.get("name");
    }
}
