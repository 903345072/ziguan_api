package com.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.jna.Native;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TdxUtil {






    public static  remote.TdxLibrary default_tdx;



    public static Map queryData(String type,String account ,String password,String ip,short port){
        RestTemplate restTemplate = new RestTemplate();
        Map<String,Object> map = new HashMap();
        Map<String,Object> map1 = new HashMap();
        map1.put("IP",ip);
        map1.put("Port",port);
        map1.put("BrokerCode",12);
        map1.put("AccountType",9);
        map1.put("YybID",0);
        map1.put("AccountNo",account);
        map1.put("TradeAccount",account);
        map1.put("JyPassword",password);
        map1.put("TxPassword",password);
        map1.put("Version","8.2");
        map.put("Auth",map1);
        map.put("EApiId","10088");
        map.put("RequestId",12345678);
        map.put("RequestType","Query");
        map.put("Subtype",type);
        map.put("data",new ArrayList());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> request = new HttpEntity<>(map, headers);
        String forObject = restTemplate.postForObject("http://47.99.126.175:9777/", request, String.class);
        Map jsonObject = JSON.parseObject(forObject);
        return jsonObject;
    }


    /**
     *
     * @param stock_code 股票代码
     * @param hand  手数
     * @param price 价格
     * @param action 买卖 1：买2：卖
     * @return
     */
    public static Map sendOrder(String stock_code,Integer hand,double price,int action,String account ,String password,String ip,short port){
        RestTemplate restTemplate = new RestTemplate();
        Map<String,Object> map = new HashMap();
        Map<String,Object> map1 = new HashMap();
        map1.put("IP",ip);
        map1.put("Port",port);
        map1.put("BrokerCode",12);
        map1.put("AccountType",9);
        map1.put("YybID",0);
        map1.put("AccountNo",account);
        map1.put("TradeAccount",account);
        map1.put("JyPassword",password);
        map1.put("TxPassword",password);
        map1.put("Version","8.2");
        map.put("Auth",map1);
        map.put("EApiId","10088");
        map.put("RequestId",12345678);
        map.put("RequestType","EnterOrder");
        map.put("Subtype","5");
        int orderType;
        String flag = stock_code.substring(0,2);
        if(flag.equals("sh")){
            orderType = 7;
        }else{
            orderType = 2;
        }
       Map data = new HashMap<>();
       data.put("symbol",stock_code.substring(2));
       data.put("exchange",stock_code.substring(0,2));
       data.put("action",action);
       data.put("orderType",1);
       data.put("Qty",hand);
       data.put("price",price);
        map.put("data",data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> request = new HttpEntity<>(map, headers);
        String forObject = restTemplate.postForObject("http://47.99.126.175:9777/", request, String.class);
        Map jsonObject = JSON.parseObject(forObject);
        return jsonObject;
    }

    public static   JSONObject  cancelOrder_(String stock_code,String orderID,String account ,String password,String ip,short port){
        RestTemplate restTemplate = new RestTemplate();
        JSONObject forObject = restTemplate.getForObject("http://47.99.126.175:82/cancelOrder/"+stock_code+"/"+orderID+"/"+account+"/"+password+"/"+ip+"/"+port, JSONObject.class);
        return forObject;
    }

}
