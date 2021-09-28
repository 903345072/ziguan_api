package com.beta.web.controller;


import com.beta.web.contextHolder.MemberHolder;
import com.stock.models.frontend.UserBill;
import com.stock.service.MemberService;
import com.stock.service.RechargeService;
import com.util.RetResponse;
import com.util.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/finace")
public class Finace {

    @Autowired
    MemberService memberService;

    @RequestMapping(value = "/getAll")
    public RetResult getAll(@RequestParam Map<String ,Object> map){
        int id = MemberHolder.getId();
        if(id != 60){
            map.put("pid",id);
        }
    List<?> lst = memberService.getFinaceListByPage(map);
    Map<String,Object> data = new HashMap<>();
    data.put("items",lst.get(0));
    data.put("count",lst.get(1));
    return RetResponse.makeOKRsp(data);
    }
}
