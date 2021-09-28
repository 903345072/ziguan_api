package listener;


import com.stock.models.frontend.Member;
import com.stock.service.MemberHeYueApply;
import com.stock.service.MemberService;
import event.makeOrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(2)
@Component
public class makeOrderListener2 implements ApplicationListener<makeOrderEvent> {

    @Autowired
    MemberService MemberServiceImpl;
    @Autowired
    MemberHeYueApply MemberHeYueApplyImpl;
    
    //增加账单记录
    @Override
    public void onApplicationEvent(makeOrderEvent makeOrderEvent) {
        Map source = (Map)makeOrderEvent.getSource();
        com.stock.models.MemberHeYueApply m = MemberHeYueApplyImpl.selectHeyueById((Integer) source.get("member_heyue_id"));
        source.put("after_amount",m.getTotal_capital());
        MemberServiceImpl.addBill(source);
        
    }
}
