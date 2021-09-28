package com.beta.web.controller;

import com.stock.models.broker;
import com.stock.service.BrokerService;
import com.util.RetResponse;
import com.util.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/broker")
public class BrokerController {
    @Autowired
    BrokerService brokerService;
    @RequestMapping("/getAll")
    public RetResult getAll(@RequestParam Map<String ,Object> map)
    {
        List<broker> list = brokerService.getAll();
        return RetResponse.makeOKRsp(list);
    }

    @RequestMapping("/getChuQuan")
    public RetResult getChuQuan(@RequestParam Map<String ,Object> map)
    {
        List<Map> list = brokerService.getChuQuan();
        return RetResponse.makeOKRsp(list);
    }

    @RequestMapping("/updateChuQuan")
    public RetResult updateChuQuan(@RequestBody Map<String ,Object> map)
    {
        brokerService.updateChuQuan(map);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping("/addChuQuan")
    public RetResult addChuQuan(@RequestBody Map<String ,Object> map)
    {
        brokerService.addChuQuan(map);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping("/deleteChuQuan")
    public RetResult deleteChuQuan(@RequestBody Map<String ,Object> map)
    {
        brokerService.deleteChuQuan(map);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping("/addBroker")
    public RetResult addBroker(@RequestBody broker broker)
    {
        int i = brokerService.addBroker(broker);
        return RetResponse.makeOKRsp(i);
    }

    @RequestMapping("/updateBroker")
    public RetResult updateBroker(@RequestBody broker broker)
    {
        int i = brokerService.updateBroker(broker);
        return RetResponse.makeOKRsp(i);
    }

    @RequestMapping("/updateStatus")
    public RetResult updateStatus(@RequestBody Map map)
    {
        int i = brokerService.updateStatus(map);
        return RetResponse.makeOKRsp(i);
    }
}
