package com.stock.service.ServiceImpl;

import com.alibaba.fastjson.JSON;
import com.mapper.WithdrawMapper;
import com.mapper.frontend.MemberMapper;
import com.stock.models.Withdraw;
import com.stock.models.frontend.Member;
import com.stock.service.WithdrawService;
import com.util.BillCode;
import event.RefuseWithdrawEvent;
import event.applyWithdrawEvent;
import event.passWithdrawEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WithdrawServiceImpl implements WithdrawService {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    MemberMapper memberMapper;
    @Autowired
    WithdrawMapper withdrawMapper;
    @Override
    public List<?> getWithdrawList(Map<String, Object> map) {
        return withdrawMapper.findWithdrawListByPage(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateWithdrawState(Map<String, Object> map) {

        int ret;
        ret  =withdrawMapper.updateWithdrawState(map);
        com.stock.models.Withdraw withdraw = findOneWithdraw((Integer) map.get("id"));
        Member user = memberMapper.findMemberById(withdraw.getUid());

        Map map_ = new HashMap();
        map_.put("member_id",user.getId());
        map_.put("amount",withdraw.getWithdraw_money());
        map_.put("type", BillCode.WIDRAW_FAILD.getCode());
        map_.put("mark", "拒绝用户"+user.getUsername()+"提现"+withdraw.getWithdraw_money()+"元");
        map_.put("link_id", map.get("id"));
        applicationEventPublisher.publishEvent((Integer)map.get("state") == 2?new RefuseWithdrawEvent(map_):new passWithdrawEvent(map_));
        return ret;
    }

    @Override
    public Withdraw findOneWithdraw(Integer id) {
        return withdrawMapper.findOneWithdraw(id);
    }
}
