package com.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class XhbUtil {






    public static  remote.TdxLibrary default_tdx;



    public static Map queryData(String api,String account ,String password,String ip,short port){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<JSONObject> forObject = restTemplate.exchange("http://"+ip+":"+port+"/"+api, HttpMethod.GET,new HttpEntity<String>(headers),JSONObject.class);
        JSONObject body = forObject.getBody();
        return body;
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
        String substring_ = stock_code.substring(0, 5);
        String code = stock_code.substring(2);
        String url = "";
        String dire = "buy";
        if(action == 2){
            dire = "sell";
        }
        if(substring_.equals("sh688") || substring_.equals("sz300")){
            url = "thsauto/"+dire+"/kc?";
        }else{
            url = "thsauto/"+dire+"?";
        }

        url = url+ "stock_no="+code+"&price="+price+"&amount="+hand;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<JSONObject> forObject = restTemplate.exchange("http://"+ip+":"+port+"/"+url, HttpMethod.GET,new HttpEntity<String>(headers),JSONObject.class);
        JSONObject body = forObject.getBody();
        return body;
    }

    public static   JSONObject  cancelOrder_(String stock_code,String orderID,String account ,String password,String ip,short port){
        RestTemplate restTemplate = new RestTemplate();
        JSONObject forObject = restTemplate.getForObject("http://"+ip+":"+port+"/"+"thsauto/cancel?entrust_no="+orderID, JSONObject.class);
        return forObject;
    }

}
