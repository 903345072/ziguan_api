package com.beta.web.controller;

import com.stock.models.User;
import com.stock.service.HeYueService;
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
@RequestMapping("/heyue")
public class HeYue {
    @Autowired
    HeYueService HeYueImpl;

    @RequestMapping("/getAll")
    public RetResult getAll(@RequestParam Map<String ,Object> map)
    {
        List HeYueList = HeYueImpl.findHeYue(map);
        int count = HeYueImpl.findHeYueCount(map);
        Map<String,Object> data = new HashMap<>();
        data.put("items",HeYueList);
        data.put("count",count);
        return RetResponse.makeOKRsp(data);
    }

    @RequestMapping("/insertOneHeYue")
    public RetResult insertOneHeYue(@RequestBody com.stock.models.HeYue heYue)
    {
        int res = HeYueImpl.insertOneHeYue(heYue);
        return RetResponse.makeOKRsp(res);
    }

    @RequestMapping("/updateHeYue")
    public RetResult updateHeYue(@RequestBody com.stock.models.HeYue heYue)
    {
        int res = HeYueImpl.updateOneHeYue(heYue);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping("/updateHeYueStatus")
    public RetResult updateHeYueStatus(@RequestBody Map map)
    {
        int res = HeYueImpl.updateHeYueStaus(map);
        return RetResponse.makeOKRsp();
    }



}
