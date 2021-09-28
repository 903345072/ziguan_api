package com.stock.service.ServiceImpl;

import com.mapper.RechargeMapper;
import com.mapper.frontend.MemberMapper;
import com.stock.models.Recharge;
import com.stock.models.form.rechargeForm;
import com.stock.models.frontend.Member;
import com.stock.service.MemberService;
import com.stock.service.RechargeService;
import com.util.BillCode;
import event.refuseRechargeEvent;
import event.passRechargeEvent;
import event.refuseRechargeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RechargeServiceImpl implements RechargeService {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    MemberService memberService;
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    RechargeMapper rechargeMapper;
    @Override
    public List<?> getRechargeList(Map<String, Object> map) {
        return rechargeMapper.findRechargeListByPage(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRechargeState(Map<String, Object> map) {

        int ret;
        ret  =rechargeMapper.updateRechargeState(map);
        Recharge recharge = findOneRecharge((Integer) map.get("id"));
        Member user = memberMapper.findMemberById(recharge.getUid());
        Map map_ = new HashMap();
        map_.put("member_id",user.getId());
        map_.put("amount",recharge.getAmount().toBigInteger());
        map_.put("type", BillCode.RECHARGE.getCode());
        map_.put("mark", "用户"+user.getUsername()+"充值"+recharge.getAmount().toBigInteger()+"元");
        map_.put("link_id", map.get("id"));
        applicationEventPublisher.publishEvent((Integer)map.get("state") == 1?new passRechargeEvent(map_):new refuseRechargeEvent(map_));
        return ret;
    }

    @Override
    public Recharge findOneRecharge(Integer id) {
        return rechargeMapper.findOneRecharge(id);
    }

    @Override
    public int dorecharge(Recharge data) {
        return rechargeMapper.dorecharge(data);
    }
}
