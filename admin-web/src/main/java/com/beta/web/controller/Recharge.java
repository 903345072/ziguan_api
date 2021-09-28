package com.beta.web.controller;


import com.beta.web.contextHolder.MemberHolder;
import com.stock.models.form.rechargeForm;
import com.stock.service.RechargeService;
import com.stock.service.WithdrawService;
import com.util.RechargeTypeCode;
import com.util.RetResponse;
import com.util.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recharge")
public class Recharge {

    @Autowired
    RechargeService RechargeServiceImpl;

    @RequestMapping(value = "/getAll")
    public RetResult getAll(@RequestParam Map<String ,Object> map){
    List<?> lst = RechargeServiceImpl.getRechargeList(map);
    Map<String,Object> data = new HashMap<>();
    data.put("items",lst.get(0));
    data.put("count",lst.get(1));
    return RetResponse.makeOKRsp(data);
    }

    @RequestMapping(value = "/updateState",method = RequestMethod.POST)
    public RetResult refuse(@RequestBody Map<String ,Object> map){

       int ret =  RechargeServiceImpl.updateRechargeState(map);
        return RetResponse.makeOKRsp(ret);
    }

    @RequestMapping(value = "/dorecharge",method = RequestMethod.POST)
    public RetResult dorecharge(@RequestBody com.stock.models.Recharge data){
        data.setUid(MemberHolder.getId());
        data.setType(3);
        data.setState(-1);
        int ret =  RechargeServiceImpl.dorecharge(data);
        return RetResponse.makeOKRsp(ret);
    }
}
