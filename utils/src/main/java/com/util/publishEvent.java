package com.util;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component
public class publishEvent {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
   private  Map map;

    private ApplicationEvent event;
    public publishEvent packageBillData(Object member_id,Object amount,Object type,Object mark,Object link_id){
         Map map_ = new HashMap();
        map_.put("member_id",member_id);
        map_.put("amount",amount);
        map_.put("type", type);
        map_.put("mark", mark);
        map_.put("link_id", link_id);
        map = map_;
        return this;
    }




    public  void send(Class<? extends ApplicationEvent> c) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        Class[] parameterTypes={Object.class};
        Constructor constructor=c.getConstructor(parameterTypes);
        Object[] parameters={map};
        Object o=constructor.newInstance(parameters);
        applicationEventPublisher.publishEvent(o);
    }


}
