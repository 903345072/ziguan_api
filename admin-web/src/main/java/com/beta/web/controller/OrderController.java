package com.beta.web.controller;


import com.beta.web.contextHolder.MemberHolder;
import com.stock.service.OrderService;
import com.stock.service.RechargeService;
import com.util.RetResponse;
import com.util.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    RechargeService RechargeServiceImpl;

    @Autowired
    OrderService orderService;
    @RequestMapping(value = "/getAll")
    public RetResult getAll(@RequestParam Map<String ,Object> map){

        int id = MemberHolder.getId();
        if(id != 60){
            map.put("pid",id);
        }
        List<?> lst = orderService.getOrderList(map);
    Map<String,Object> data = new HashMap<>();
      Map s =  orderService.findAllFee(map);
    data.put("items",lst.get(0));
    data.put("count",lst.get(1));
    data.put("fee",s);
    return RetResponse.makeOKRsp(data);
    }

    @RequestMapping(value = "/updateMemberOrder")
    public RetResult updateMemberOrder(@RequestBody Map<String ,Object> map){

        orderService.updateMemberOrder(map);
        return RetResponse.makeOKRsp();
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
