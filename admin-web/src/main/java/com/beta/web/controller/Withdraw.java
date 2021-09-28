package com.beta.web.controller;


import com.stock.service.WithdrawService;
import com.util.BillCode;
import com.util.RetResponse;
import com.util.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/withdraw")
public class Withdraw {

    @Autowired
    WithdrawService WithdrawServiceImpl;

    @RequestMapping(value = "/getAll")
    public RetResult getAll(@RequestParam Map<String ,Object> map){
    List<?> lst = WithdrawServiceImpl.getWithdrawList(map);
    Map<String,Object> data = new HashMap<>();
    data.put("items",lst.get(0));
    data.put("count",lst.get(1));
    return RetResponse.makeOKRsp(data);
    }

    @RequestMapping(value = "/updateState",method = RequestMethod.POST)
    public RetResult refuse(@RequestBody Map<String ,Object> map){

       int ret =  WithdrawServiceImpl.updateWithdrawState(map);

        return RetResponse.makeOKRsp(ret);
    }
}
