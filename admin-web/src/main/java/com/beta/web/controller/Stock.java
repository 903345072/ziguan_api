package com.beta.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beta.web.contextHolder.MemberHolder;
import com.show.api.ShowApiRequest;
import com.stock.models.frontend.optional;
import com.stock.service.OptionalService;
import com.stock.service.StockDataServiceAbstract;
import com.util.RetResponse;
import com.util.RetResult;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stock")
public class Stock {

    @Qualifier("sina")
    @Autowired
    StockDataServiceAbstract SinaStockServiceImpl;
    @Autowired
    OptionalService OptionalImpl;
    @Value("${wwyy.appid}")     private String appid;
    @Value("${wwyy.appSecret}")     private  String appSecret;
    @RequestMapping("/getTimeSharingData/{stock_code}")
    public RetResult get(@PathVariable String stock_code) {


        //分时线数据
        String res=new ShowApiRequest("http://route.showapi.com/131-49",appid,appSecret)
                .addTextPara("code",stock_code)
                .addTextPara("day","1")
                .post();
        //实时数据
        String res1=new ShowApiRequest("http://route.showapi.com/131-46",appid,appSecret)
                .addTextPara("stocks",stock_code)
                .addTextPara("needIndex","0")
                .post();
        HashMap<String,Object> hm = new HashMap<String,Object>();
        hm.put("data",res);
        hm.put("data1",res1);

        return RetResponse.makeOKRsp(hm);
    }

    @RequestMapping("/getKdata/{period}/{stock_code}")
    public RetResult getKdata(@PathVariable String period,@PathVariable String stock_code) {

        String res=new ShowApiRequest("http://route.showapi.com/131-50",appid,appSecret)
                        .addTextPara("code",stock_code)
                        .addTextPara("time",period)
                        .addTextPara("beginDay","20180801")
                        .addTextPara("type","bfq")
                        .post();
        HashMap<String,Object> hm = new HashMap<String,Object>();
        hm.put("data",res);
        return RetResponse.makeOKRsp(hm);
    }

    @RequestMapping("/getRankList/{page}")
    public RetResult getRankList(@PathVariable String page) {
        String res=new ShowApiRequest("http://route.showapi.com/131-64",appid,appSecret)
                .addTextPara("sortFeild","diff_rate")
                .addTextPara("sortType","-1")
                .addTextPara("page",page)
                .post();

        HashMap<String,Object> hm = new HashMap<String,Object>();
        hm.put("data",res);
        return RetResponse.makeOKRsp(hm);
    }

    @RequestMapping("/getDaPanData")
    public RetResult getDaPanData() {
        //大盘数据
        String res=new ShowApiRequest("http://route.showapi.com/131-45",appid,appSecret)
                .addTextPara("stocks","sh000001,sz399001,sz399006")
                .post();
        HashMap<String,Object> hm = new HashMap<String,Object>();
        //龙虎榜数据
        hm.put("data",res);
        return RetResponse.makeOKRsp(hm);
    }
    @RequestMapping("/getTradeData/{stockCode}")
    public RetResult getTradeData(@PathVariable String stockCode) {
        //大盘数据
        String res=new ShowApiRequest("http://route.showapi.com/131-46",appid,appSecret)
                .addTextPara("stocks",stockCode)
                .addTextPara("needIndex","0")
                .post();
        String res1=new ShowApiRequest("http://route.showapi.com/131-54",appid,appSecret)
                .addTextPara("code",stockCode)
                .post();
        HashMap<String,Object> hm = new HashMap<String,Object>();
        //龙虎榜数据
        hm.put("data",res);
        hm.put("data1",res1);
        return RetResponse.makeOKRsp(hm);
    }


    @RequestMapping("/optional_list/{list}")
    public RetResult optional_list(@PathVariable String list) {

        String res1=new ShowApiRequest("http://route.showapi.com/131-46",appid,appSecret)
                .addTextPara("stocks",list)
                .post();
        HashMap<String,Object> hm = new HashMap<String,Object>();
        hm.put("data1",res1);
        return RetResponse.makeOKRsp(hm);
    }

    //根据股票代码，拼音搜索股票
    @RequestMapping(value = "/searchStock")
    public RetResult searchStock(@RequestParam String stock_code) {
        //大盘数据
        String res=new ShowApiRequest("http://route.showapi.com/131-43",appid,appSecret)
                .addTextPara("code",stock_code)
                .post();
        HashMap<String,Object> hm = new HashMap<String,Object>();
        //龙虎榜数据
        hm.put("data",res);
        return RetResponse.makeOKRsp(hm);
    }

    //加自选
    @RequestMapping(value = "/add_optional")
    public RetResult add_optional(@RequestBody optional optional) {

        int id = MemberHolder.getId();
        optional.setUid(id);
        int i = OptionalImpl.add_optional(optional);
        return RetResponse.makeOKRsp(i);
    }

    //删自选
    @RequestMapping(value = "/delete_optional")
    public RetResult delete_optional(@RequestBody optional optional) {

        int id = MemberHolder.getId();
        optional.setUid(id);
        int i = OptionalImpl.delete_optional(optional);
        return RetResponse.makeOKRsp(i);
    }

    @RequestMapping(value = "/get_is_optional")
    public RetResult get_is_optional(@RequestParam HashMap<String,Object> map) {
        int id = MemberHolder.getId();
        map.put("uid",id);
        optional optional = OptionalImpl.find_optional(map);
        int stock_id = 0;
        if(optional != null){
          stock_id = optional.getId();
        }
        return RetResponse.makeOKRsp(stock_id);
    }

    @RequestMapping(value = "/get_my_optional")
    public RetResult get_my_optional() {
        Integer id = MemberHolder.getId();
        List<String> my_optional = OptionalImpl.get_my_optional(id);

        String join = String.join(",", my_optional.stream().map(String::valueOf).collect(Collectors.toList()));
        String res1=new ShowApiRequest("http://route.showapi.com/131-46",appid,appSecret)
                .addTextPara("stocks", join)
                .post();
        HashMap<String,Object> hm = new HashMap<String,Object>();
        JSONObject jsonObject = JSON.parseObject(res1);
        hm.put("data1",jsonObject);
        return RetResponse.makeOKRsp(hm);
    }
    @RequestMapping(value = "/getNowPrice/{code}")
    public RetResult getNowPrice(@PathVariable String code){
        BigDecimal stockPrice = SinaStockServiceImpl.setDataSource(code).getStockPrice();
        return RetResponse.makeOKRsp(stockPrice);
    }
}
