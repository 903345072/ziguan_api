package com.stock.service.ServiceImpl;

import com.show.api.NormalRequest;
import com.stock.service.StockDataService;
import com.stock.service.StockDataServiceAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Qualifier("sina")
public class SinaStockServiceImpl extends StockDataServiceAbstract {

    private String price_url = "http://hq.sinajs.cn/list=s_";

    @Override
    public BigDecimal getStockPrice() {
        String s = request.get();
        parseString(s);
        return stock_price;
    }

    @Override
    public StockDataServiceAbstract setDataSource(String code_){
        code = code_;
        request = new NormalRequest(price_url+code);
        return this;
    }

    @Override
    public BigDecimal getStockRate() {
        String s = request.get();
        parseRateString(s);
        return stock_rate;
    }

    @Override
    public void parseString(String str) {
        String pattern = "var hq_str_s_([sh|sz]{2})(\\d{6})=\"(.*)\"";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        if(m.find()){
            if(m.groupCount() == 3){
                String str_split = m.group(3);
                String[] split = str_split.split(",");
                stock_price = new BigDecimal(split[1]);
            }

        }

    }

    @Override
    public void parseRateString(String str) {

        String pattern = "var hq_str_s_([sh|sz]{2})(\\d{6})=\"(.*)\"";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        if(m.find()){
            if(m.groupCount() == 3){
                String str_split = m.group(3);
                String[] split = str_split.split(",");
                stock_rate = new BigDecimal(split[3]);
            }

        }
    }
}
