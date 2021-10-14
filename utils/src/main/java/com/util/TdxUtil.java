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






    public static Map queryData(String type,String account ,String password,String tx_password,String ip,short port){
        RestTemplate restTemplate = new RestTemplate();
        JSONObject forObject = restTemplate.getForObject("http://8.134.124.134:82/queryData/"+account+"/"+password+"/"+tx_password+"/"+ip+"/"+port+"/"+type, JSONObject.class);
        return forObject;
    }

    /**
     *
     * @param stock_code 股票代码
     * @param hand  手数
     * @param price 价格
     * @param action 买卖 1：买2：卖
     * @return
     */
    public static Map sendOrder(String stock_code,Integer hand,double price,int action,String account ,String password,String tx_password,String ip,short port,String gddm){
        RestTemplate restTemplate = new RestTemplate();
        JSONObject forObject = restTemplate.getForObject("http://8.134.124.134:82/sendOrder/"+account+"/"+password+"/"+tx_password+"/"+ip+"/"+port+"/"+price+"/"+hand+"/"+action+"/"+stock_code+"/"+gddm, JSONObject.class);
        return forObject;
    }

    public static   Map  cancelOrder_(String stock_code,String orderID,String account ,String password,String tx_password,String ip,short port){
        RestTemplate restTemplate1 = new RestTemplate();
        JSONObject forObject1 = restTemplate1.getForObject("http://8.134.124.134:82/cancelOrder/sh601258/12/036000000592/933150/933150/58.211.94.45/7708", JSONObject.class);
        return forObject1;
    }

}
