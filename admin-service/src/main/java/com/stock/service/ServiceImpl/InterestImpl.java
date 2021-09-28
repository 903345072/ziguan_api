package com.stock.service.ServiceImpl;


import com.alibaba.fastjson.JSON;
import com.mapper.HeYueMapper;
import com.mapper.InterestMapper;
import com.mapper.LeverageMapper;
import com.stock.models.HeYue;
import com.stock.models.Interest;
import com.stock.service.HeYueService;
import com.stock.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InterestImpl implements InterestService {
    @Autowired
    InterestMapper interestMapper;

    @Autowired
    HeYueMapper heYueMapper;

    @Autowired
    LeverageMapper leverageMapper;

    @Override
    public List findInterest() {
        List hyIds = heYueMapper.findValidHeYueIdList();
        List leverIds = leverageMapper.findValidLeverageIdList();
        List<Map> mapList = new ArrayList<>();
        hyIds.forEach(e -> {
            leverIds.forEach(v -> {
                Map map = new HashMap();
                Integer hid = (Integer) ((HashMap) e).get("id");
                Integer lid = (Integer) ((HashMap) v).get("id");
                map.put("hid", hid);
                map.put("lid", lid);
                Interest interest = interestMapper.findInterestByCase(map);
                String hname = interest != null ? interest.getHeYue().getName() : (String) heYueMapper.findHeYueNameById(hid).get("name");
                double lname = interest != null ? interest.getLeverage().getName() : (double) leverageMapper.findLeverageNameById(lid).get("name");
                double rate = interest != null ? interest.getRate() : 0;
                map.put("hname", hname);
                map.put("lname", lname);
                map.put("rate", rate);
                mapList.add(map);
            });
        });
        return mapList;
    }

    @Override
    public int setInterest(List list) {
        deleteAllInterest();
        return interestMapper.setInterest(list);
    }

    @Override
    public int deleteAllInterest() {
        return interestMapper.deleteAllInterest();
    }

    @Override
    public Map findInterestByIds(int heyue_id, int leverage_id) {
        return interestMapper.findInterestByIds(heyue_id, leverage_id);
    }
}
