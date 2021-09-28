package listener;

import com.mapper.frontend.MemberHeYueApplyMapper;
import com.stock.models.frontend.Member;
import com.stock.service.MemberHeYueApply;
import com.stock.service.MemberService;
import com.stock.service.ServiceImpl.HeYueCaculateAdpterInterface;
import com.stock.service.ServiceImpl.MemberServiceImpl;
import com.stock.service.rabbitmq.OrderRabbitMQConfig;
import com.stock.service.rabbitmq.productor.OrderProductor;
import com.util.BillCode;
import com.util.Holiday;
import event.PassApplyHeYueEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class passHeYueApplyListener1 implements ApplicationListener<PassApplyHeYueEvent> {
    //修改合约使用时间,并加入延时对列任务，使用时间到了后过期
    @Autowired
    MemberHeYueApply MemberHeYueApplyImpl;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    OrderProductor orderProductor;

    @Autowired
    com.stock.service.ServiceImpl.MemberServiceImpl ms;

    @Autowired
    @Qualifier("HeYueCaculateAdper")
    HeYueCaculateAdpterInterface HeYueCaculateImpl;

    @Override
    public void onApplicationEvent(PassApplyHeYueEvent passApplyHeYueEvent) {
        Map source = (Map) passApplyHeYueEvent.getSource();
       // MemberHeYueApplyImpl.updateHeYueStartTime((Integer) source.get("link_id"));

        //计算节假日延期多久
        com.stock.models.MemberHeYueApply heyue = MemberHeYueApplyImpl.selectHeyueById((Integer) source.get("link_id"));
        //累计利息
        double rate = HeYueCaculateImpl.cal_interest_rate(heyue.getHeyue_id(),heyue.getLeverage_id());
        int base_num = HeYueCaculateImpl.cal_capitial_used_time(heyue.getHeyue_id());
        double leverage_money = heyue.getLeverage_money();
        double interest = HeYueCaculateImpl.cal_interest(rate, leverage_money, base_num);
        Map datas = new HashMap();
        datas.put("link_id",source.get("link_id"));
        datas.put("amount",interest);
        int i=  MemberHeYueApplyImpl.addMemberInterest(datas);
      Member member = ms.findMemberById(heyue.getMember_id());

        Map map_ = new HashMap();
        map_.put("member_id",heyue.getMember_id());
        map_.put("link_id",heyue.getId());
        map_.put("amount",interest);
        map_.put("type", BillCode.EXTENSION_FEE.getCode());
        map_.put("after_amount", member.getAmount());
        map_.put("mark", "用户"+member.getId()+"利息消费"+new BigDecimal(interest).setScale(2, RoundingMode.HALF_UP) +"元");
        ms.addBill(map_);

        Date date = new Date();
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd 14:50:00");
        String limit_format = formatter.format(date);

        SimpleDateFormat formatter_  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String cur_format = formatter_.format(date);
        //若已经过了两点50,或者今天是节假日，此合约下个交易日生效
        Calendar cal = Calendar.getInstance();
        if(limit_format.compareTo(cur_format) <0 || !Holiday.is_trade_day(cal)){
            Map data = new HashMap();
            data.put("apply_state",0);
            data.put("id",(Integer) source.get("link_id"));
            MemberHeYueApplyImpl.updateHeYueApplyState(data);
            SimpleDateFormat sdf = new SimpleDateFormat("M-d");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);//-1.昨天时间 0.当前时间 1.明天时间 *以此类推
            String tomorrow_day = sdf.format(c.getTime()); //7-1
            //判断明天是不是
            while (true){
                String[] split = tomorrow_day.split("-");
                Integer month_ = Integer.parseInt(split[0]);
                Integer day_ = Integer.parseInt(split[1]);
                List lists = Holiday.map.get(month_);
                if(lists.contains(day_)){//说明明天是节假日
                    c.add(Calendar.DATE, 1);
                    tomorrow_day = sdf.format(c.getTime());
                }else{
                    break;
                }
            }
            String[] split = tomorrow_day.split("-");
            Integer month_ = Integer.parseInt(split[0]);
            Integer day_ = Integer.parseInt(split[1]);
            Calendar cc = Calendar.getInstance();
            cc.clear();
            cc.setTime(new Date());
            cc.set(Calendar.MONTH,month_-1);
            cc.set(Calendar.DATE,day_);
            cc.set(Calendar.HOUR,-12);
            cc.set(Calendar.MINUTE,0);
            cc.set(Calendar.SECOND,0);
            SimpleDateFormat formatter1  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String effect_time = formatter1.format(cc.getTime());
            data.put("start_time",effect_time);
            MemberHeYueApplyImpl.updateHeYueStartTime(data);
        }else{
            ms.xuyue((Integer) source.get("link_id"));
        }
    }
}
