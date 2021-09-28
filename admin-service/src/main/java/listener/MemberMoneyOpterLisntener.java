package listener;

import com.mapper.frontend.MemberMapper;
import event.RefuseWithdrawEvent;
import event.applyWithdrawEvent;
import event.withdrawEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

@Order(1)
@Component
public class MemberMoneyOpterLisntener<T extends withdrawEvent> implements ApplicationListener<T> {

    @Autowired
    MemberMapper memberMapper;
    @Override
    public void onApplicationEvent(T t) {

        Class z = t.getClass();
        Map source = (Map)t.getSource();
        try {
            Field direction = z.getDeclaredField("direction");
            if((int)direction.get(z) == -1){
                memberMapper.decreaseMemberAmount(source);
            }else if((int)direction.get(z) == 1){
                memberMapper.increaseMemberAmount(source);
            }
        }catch (NoSuchFieldException | IllegalAccessException e){
            try {
                throw e;
            } catch (NoSuchFieldException | IllegalAccessException noSuchFieldException) {
                noSuchFieldException.printStackTrace();
            }
        }




    }
}
