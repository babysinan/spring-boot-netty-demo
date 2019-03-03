package com.sinostage.socket.server;

import com.sinostage.socket.protobuf.UserMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author timkobe
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    Logger log = LoggerFactory.getLogger(NettyServerHandler.class);
    /**
     * 建立连接时，发送一条消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接的客户端地址:" + ctx.channel().remoteAddress());
        UserMsg.User user = UserMsg.User.newBuilder().setId(1).setAge(24).setName("穆书伟").setState(0).build();
        ctx.writeAndFlush(user);
        super.channelActive(ctx);
    }

    /**
     * 超时处理 如果5秒没有接受客户端的心跳，就触发; 如果超过两次，则直接关闭;
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            // 如果读通道处于空闲状态，说明没有接收到心跳命令
            if (IdleState.READER_IDLE.equals(event.state())) {
                log.info("已经5秒没有接收到客户端的信息了");
//                if (idle_count.get() > 1) {
//                    log.info("关闭这个不活跃的channel");
//                    ctx.channel().close();
//                }
//                idle_count.getAndIncrement();
            }
        } else {
            super.userEventTriggered(ctx, obj);
        }
    }

    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            log.info("ddd:"+msg.toString());
            // 如果是protobuf类型的数据
            if (msg instanceof UserMsg.User) {
                UserMsg.User user = (UserMsg.User) msg;
                if (user.getState() == 1) {

                } else if(user.getState() == 2){

                }else{

                }
            } else {

                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
