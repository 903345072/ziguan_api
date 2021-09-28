import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beta.web.BetaWebApplication;
import com.mapper.PermissionMapper;
import com.mapper.RoleMapper;
import com.mapper.UserMapper;
import com.mapper.WithdrawMapper;
import com.mapper.frontend.MemberHeYueApplyMapper;
import com.mapper.frontend.OrderMapper;
import com.stock.models.*;
import com.stock.models.entity.order;
import com.stock.models.frontend.nettyOrder;
import com.stock.service.*;

import com.stock.service.rabbitmq.productor.OrderProductor;
import com.sun.jna.Native;
import com.util.Holiday;
import com.util.TdxUtil;
import org.apache.commons.httpclient.URI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(classes = BetaWebApplication.class)
@RunWith(SpringRunner.class)
public class test1 {


    @Autowired
    PermissionMapper permissionMapper;
@Autowired
    OrderMapper o;
    @Qualifier("wwyy")
    @Autowired
    StockDataServiceAbstract SinaStockServiceImpl;
    @Autowired
    WithdrawMapper withdrawMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    OrderProductor orderProductor;
    @Autowired
    MemberHeYueApplyMapper m;
    @Autowired
    BrokerService brokerServiceImpl;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate_;
    @Autowired
    OrderService orderServiceImpl;
//[{children=[{children=[{children=[{children=[], id=31}], id=19}], id=18}, {children=[], id=30}], id=13}]
    public void findChild(ArrayList<Map<String ,Object>> permissions, ArrayList<Integer> lists){
        for(Map<String,Object> map : permissions){
                 lists.add((Integer) map.get("id"));
            ArrayList<Map<String ,Object>> pms = (ArrayList<Map<String ,Object>>) map.get("children");
            findChild(pms,lists);
        }
    }

    @Test
    //@Transactional(rollbackFor=Exception.class)
    public void queryData(){
                Map dd = new HashMap();
                dd.put("stock_code","asd");
                dd.put("price",3.12);
                brokerServiceImpl.addChuQuan(dd);
    }
    @Test
    public void queryData1(){
        List<Chengjiao> chengjiaos = orderServiceImpl.NoChengjiaoByContractNo("167788");//800
        List<Chengjiao> no_cj = chengjiaos.stream().filter(k -> k.getStatus() == 0).collect(Collectors.toList());//100
        orderServiceImpl.updateChengJiaoStatus(no_cj);
    }

}
