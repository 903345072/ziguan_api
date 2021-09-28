package com.beta.web.aspect;

import com.beta.web.contextHolder.MemberHolder;
import com.beta.web.exception.InsufficientMoneyExceptioin;
import com.mapper.frontend.MemberMapper;
import com.stock.models.frontend.Member;
import com.stock.service.MemberService;
import com.stock.service.OrderService;
import com.stock.service.ServiceImpl.HeYueCaculateAdpterInterface;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class FengKongAspect {

    @Autowired
    OrderService orderServiceImpl;

    @Autowired
    MemberService memberServiceImpl;

    @Pointcut("execution(public * com.beta.web.controller.FrontendApi.Order.makeOrder(..))")
    public void pointcut(){}


    public static Map<String, Object> getKeyAndValue(Object obj) {
        Map<String, Object> map = new HashMap<>();
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val = new Object();
            try {
                val = f.get(obj);
                // 得到此属性的值
                map.put(f.getName(), val);// 设置键值
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return map;
    }

    @Before("pointcut()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String method = request.getMethod();
        Object[] args = joinPoint.getArgs();
        Map order_data = new HashMap();
        if(args.length>0){
            if("POST".equals(method)){
                Object object = args[0];
              order_data = getKeyAndValue(object);

            }
        }
        String stock_code = (String) order_data.get("stock_code");
        Integer buy_hand = (Integer) order_data.get("buy_hand");
        String substring_ = stock_code.substring(0, 5);
        int id = MemberHolder.getId();
        Map data = memberServiceImpl.findMemberFengKong(id);
        Map map = new HashMap();
        map.put("uid",id);
        map.put("stock_code",stock_code);
        Integer stock_hand = orderServiceImpl.findStockHand(map);
        if(substring_.equals("sh688") || substring_.equals("sz300")){
            //没
            Integer is_kc = (Integer) data.get("is_kc");
            Integer kc_max = (Integer) data.get("kc_max");
            if(is_kc == 1){
                Integer all_hand = buy_hand+stock_hand;
                if(all_hand > kc_max){
                    throw new RuntimeException("创业/科创板持仓上限为"+kc_max);
                }
            }else{
                throw new RuntimeException("您无法购买创业/科创板的股票");
            }
        }
        Integer single_max = (Integer) data.get("single_max");
        Integer all_hand = buy_hand+stock_hand;
        if(all_hand > single_max){
            throw new RuntimeException("单持仓上限为"+single_max);
        }
        String forbid_list = (String) data.get("forbid_list");
        if(forbid_list.contains(stock_code)){
            throw new RuntimeException("您无权购买此股");
        }

    }

}