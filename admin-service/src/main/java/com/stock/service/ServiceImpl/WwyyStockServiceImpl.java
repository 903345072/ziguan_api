package com.stock.service.ServiceImpl;

import com.alibaba.fastjson.JSON;
import com.show.api.NormalRequest;
import com.show.api.ShowApiRequest;
import com.stock.service.StockDataServiceAbstract;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Qualifier("wwyy")
public class WwyyStockServiceImpl extends StockDataServiceAbstract {

    private String price_url = "http://route.showapi.com/131-46";
    @Value("${wwyy.appid}")     private String appid;
    @Value("${wwyy.appSecret}")     private  String appSecret;
    @Override
    public BigDecimal getStockPrice() {

        request.addTextPara("stocks",code)
                .addTextPara("needIndex","0");
        String s = request.post();
        parseString(s);
        return stock_price;
    }

    @Override
    public StockDataServiceAbstract setDataSource(String code_){
        String pattern = "[0-9]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(code_);
        if(m.find()){
            code = code_.trim();
        }

        request = new ShowApiRequest(price_url,appid,appSecret);
        return this;
    }

    @Override
    public BigDecimal getStockRate() {
        request.addTextPara("stocks",code)
                .addTextPara("needIndex","0");
        String s = request.post();
        parseRateString(s);
        return stock_rate;
    }

    @Override
    public void parseString(String str) {
        Map str1 = (Map)JSON.parse(str);
       if(str1.get("showapi_res_code").toString().equals("0")){
          Map str2 = (Map)JSON.parse(str1.get("showapi_res_body").toString());
          List s = (List) str2.get("list");
          Map str3 = (Map)JSON.parse(s.get(0).toString());
          stock_price = new BigDecimal(str3.get("nowPrice").toString());
       }
    }

    @Override
    public void parseRateString(String str) {
        Map str1 = (Map)JSON.parse(str);
        if(str1.get("showapi_res_code").toString().equals("0")){
            Map str2 = (Map)JSON.parse(str1.get("showapi_res_body").toString());
            List s = (List) str2.get("list");
            Map str3 = (Map)JSON.parse(s.get(0).toString());
            stock_rate = new BigDecimal(str3.get("diff_rate").toString());
        }
    }
}
