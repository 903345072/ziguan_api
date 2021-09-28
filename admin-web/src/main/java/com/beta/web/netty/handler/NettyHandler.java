package com.beta.web.netty.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.stock.models.frontend.Order;
import com.stock.models.frontend.nettyOrder;
import com.stock.service.OrderService;
import com.stock.service.StockDataService;
import com.stock.service.StockDataServiceAbstract;
import com.util.RequestUriUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
public class NettyHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {//TextWebSocketFrame是netty用于处理websocket发来的文本对象
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static int online;
    public int readIdelTimes;

    public static NettyHandler nettyHandler;
    @Autowired
    private RedisTemplate redisTemplate_;

    @Autowired
    OrderService OrderServiceImpl;

    @Autowired
    @Qualifier(value = "sina")
    StockDataServiceAbstract wwyy;

    @PostConstruct
    public void init() {
        nettyHandler = this;
    }
    private static volatile boolean flag = false;


    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        SendAllMessages(ctx,msg);//send_message是我的自定义类型，前后端分离往往需要统一数据格式，可以先把对象转成json字符串再发送给客户端
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {
            fullHttpRequestHandler(ctx, (FullHttpRequest) msg);
        }
        ctx.fireChannelRead(msg);
        //super.channelRead(ctx, msg);
    }



    private void fullHttpRequestHandler(ChannelHandlerContext ctx, FullHttpRequest request) {
        String uri = request.getUri();
        Map<String, String> params = RequestUriUtils.getParams(uri);
        String path = params.get("path");
        Integer member_heyue_id = Integer.parseInt(params.get("hid"));
        if(path.equals("getOrder")){
            Runnable send =  new Runnable(){
                @Override
                public void run(){
                    while (online>0){
                        try {
                            Set keys = nettyHandler.redisTemplate_.opsForHash().keys(member_heyue_id.toString());
                            if(keys.size()>0){

                                Map entries = nettyHandler.redisTemplate_.opsForHash().entries(member_heyue_id.toString());
                                entries.forEach((k,v)->{
                                    nettyOrder order = (nettyOrder) JSON.parseObject((String) v,new TypeReference<nettyOrder>(){});
                                    setOrderData(order,member_heyue_id);
                                });
                            }else{
                                //查询此合约所有订单信息，
                                Map m = new HashMap<>();
                                m.put("heyue_id",member_heyue_id);
                                m.put("stock_status",2);
                                List order_list = nettyHandler.OrderServiceImpl.findAllOrderByCase(m);
                                order_list.forEach(s->{
                                    nettyOrder order = (nettyOrder) s;
                                    setOrderData(order,member_heyue_id);
                                });
                            }

                            Map entries_ = nettyHandler.redisTemplate_.opsForHash().entries(member_heyue_id.toString());
                            if(ctx.channel().isActive()){
                                SendMessage(ctx,JSON.toJSONString(entries_));
                            }
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                           // e.printStackTrace();
                        }
                    }
                }
            };

            Thread thread = new Thread(send);
            thread.start();
            flag = true;

        }

        // 判断请求路径是否跟配置中的一致

    }


    public void setOrderData(nettyOrder order,Integer member_heyue_id){
      nettyHandler.OrderServiceImpl.setOrder(order);
      nettyHandler.redisTemplate_.opsForHash().put(member_heyue_id.toString(),Integer.toString(order.getId()), JSON.toJSONString(order));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,Object evt){
           if(evt instanceof IdleStateEvent){
               IdleStateEvent   event = (IdleStateEvent) evt;
               String eventType = null;
               switch (event.state()){
                   case ALL_IDLE:
                       eventType="读写空闲";
                       break;
                   case READER_IDLE:
                       eventType="读空闲";
                       readIdelTimes+=1;
                       break;
                   case WRITER_IDLE:
                       eventType="写空闲";
                       break;
               }
               if(readIdelTimes>3){  //发生超时，默认认为客户端断开连接
                   System.out.println("超时三次，断开连接");
                   ctx.channel().close();
               }
           }

    }
//    @Override
//    public void channelActive(final ChannelHandlerContext ctx) throws InterruptedException {
//      Runnable send =  new Runnable(){
//            @Override
//            public void run(){
//                  while (true){
//                      try {
//                          SendMessage(ctx,"213");
//                          Thread.sleep(5000);
//                      } catch (InterruptedException e) {
//                          e.printStackTrace();
//                      }
//                  }
//            }
//        };
//
//      new Thread(send).start();
//
//    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channelGroup.add(ctx.channel());
        online=channelGroup.size();
        System.out.println(ctx.channel().remoteAddress()+"上线了!");
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.remove(ctx.channel());
        online=channelGroup.size();
        System.out.println(ctx.channel().remoteAddress()+"断开连接");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //cause.printStackTrace();
    }
    private void SendMessage(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
    }
    private void SendAllMessages(ChannelHandlerContext ctx,TextWebSocketFrame msg) {
        for(Channel channel:channelGroup){
            if(channelGroup.contains(channel)){
                channel.writeAndFlush(msg);
            }

//            if(!channel.id().asLongText().equals(ctx.channel().id().asLongText())){
//                channel.writeAndFlush(new TextWebSocketFrame(msg));
//            }
        }
    }

}
