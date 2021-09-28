package listener;

import com.mapper.frontend.MemberMapper;
import com.stock.models.frontend.Member;
import event.RefuseWithdrawEvent;
import event.applyWithdrawEvent;
import event.passWithdrawEvent;
import event.withdrawEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Map;

@Order(2)
@Component
public class addBillLisntener<T extends withdrawEvent> implements ApplicationListener<T> {

    @Autowired
    MemberMapper memberMapper;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public  void onApplicationEvent(T t) {
        Class z = t.getClass();
        Map source = (Map)t.getSource();
        try {
            Field direction = z.getDeclaredField("is_addbill");
            if((int)direction.get(z) == 1){
                Member member = memberMapper.findMemberById((Integer) source.get("member_id"));
                source.put("after_amount",member.getAmount());
                memberMapper.addBill(source);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            try {
                throw e;
            } catch (NoSuchFieldException | IllegalAccessException noSuchFieldException) {
                noSuchFieldException.printStackTrace();
            }
        }
    }
}
