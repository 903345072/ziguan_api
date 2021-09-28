package com.beta.web.controller;

import com.stock.service.LeverageService;
import com.stock.service.LeverageService;
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
@RequestMapping("/leverage")
public class Leverage {
    @Autowired
    LeverageService LeverageImpl;

    @RequestMapping("/getAll")
    public RetResult getAll(@RequestParam Map<String ,Object> map)
    {
        List LeverageList = LeverageImpl.findLeverage(map);
        int count = LeverageImpl.findLeverageCount(map);
        Map<String,Object> data = new HashMap<>();
        data.put("items",LeverageList);
        data.put("count",count);
        return RetResponse.makeOKRsp(data);
    }

    @RequestMapping("/insertOneLeverage")
    public RetResult insertOneLeverage(@RequestBody com.stock.models.Leverage Leverage)
    {
        int res = LeverageImpl.insertOneLeverage(Leverage);
        return RetResponse.makeOKRsp(res);
    }

    @RequestMapping("/updateLeverage")
    public RetResult updateLeverage(@RequestBody com.stock.models.Leverage Leverage)
    {
        int res = LeverageImpl.updateOneLeverage(Leverage);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping("/updateLeverageStatus")
    public RetResult updateLeverageStatus(@RequestBody Map map)
    {
        int res = LeverageImpl.updateLeverageStaus(map);
        return RetResponse.makeOKRsp();
    }

}
