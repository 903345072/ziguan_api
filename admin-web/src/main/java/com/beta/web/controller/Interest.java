package com.beta.web.controller;

import com.stock.service.InterestService;
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
@RequestMapping("/interest")
public class Interest {
    @Autowired
    InterestService InterestImpl;

    @RequestMapping("/getAll")
    public RetResult getAll(@RequestParam Map<String ,Object> map)
    {
        List LeverageList = InterestImpl.findInterest();
        Map<String,Object> data = new HashMap<>();
        data.put("items",LeverageList);
        return RetResponse.makeOKRsp(data);
    }

    @RequestMapping("/setInterest")
    public RetResult setInterest(@RequestBody Map<String ,Object> map)
    {
        List interestList = (List) map.get("data");
        int res = InterestImpl.setInterest(interestList);
        return RetResponse.makeOKRsp(res);
    }
}
