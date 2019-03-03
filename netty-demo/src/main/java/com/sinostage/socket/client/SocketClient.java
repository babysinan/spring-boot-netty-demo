package com.sinostage.socket.client;

import com.sinostage.socket.protobuf.UserMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @author timkobe
 */
public class SocketClient {
    Logger log = LoggerFactory.getLogger(SocketClient.class);
//    @Value("${server.bind_address}")
    private String host="127.0.0.1";

//    @Value("${server.bind_port}")
    private Integer port=8088;

    /**唯一标记 */
    private boolean initFalg=true;

    private EventLoopGroup group;
    private ChannelFuture f;

    private Channel channel;

    /**
     * Netty创建全部都是实现自AbstractBootstrap。 客户端的是Bootstrap，服务端的则是 ServerBootstrap。
     **/
    @PostConstruct
    public void init() {
        group = new NioEventLoopGroup();
        doConnect(new Bootstrap(), group);
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {

        try {
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }
    /**
     * 重连
     */
    public void doConnect(Bootstrap bootstrap, EventLoopGroup eventLoopGroup) {
        try {
            if (bootstrap != null) {
                bootstrap.group(eventLoopGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                bootstrap.handler(new NettyClientInitializer());
                bootstrap.remoteAddress(host, port);
                ChannelFuture connect = bootstrap.connect();

                channel = connect.channel();
//                f = bootstrap.connect().addListener((ChannelFuture futureListener) -> {
//                    final EventLoop eventLoop = futureListener.channel().eventLoop();
//                    if (!futureListener.isSuccess()) {
//                        log.info("与服务端断开连接!在10s之后准备尝试重连!");
//                        eventLoop.schedule(() -> doConnect(new Bootstrap(), eventLoop), 10, TimeUnit.SECONDS);
//                    }
//                });
//                if(initFalg){
//                    log.info("Netty客户端启动成功!");
//                    initFalg=false;
//                }
            }
        } catch (Exception e) {
            log.info("客户端连接失败!"+e.getMessage());
        }
    }
    public void run() {
//
        try {
        for (short i=0; true; i++) {
//        	Message msg = new Message(1, 40001, i, "{\"verCode\":\"2.0\"}");
//        	Message msg = new Message(1, 10004, i, "{\"sortid\":2,\"orderby\":1,\"timetype\":1,\"pi\":1}");
//        	Message msg = new Message(1, 10005, i, "{\"localdev\":\"Huawei C8500\",\"dev\":\"Huawei C8500\",\"fac\":\"HTC\",\"mac\":\"356871048965853\",\"uuid\":\"39e67fec-e527-4551-9b4c-b0bba179cc4e\",\"cid\":\"AndroidClient\",\"version\":\"15\"}");
            //Message msg = new Message(1, 2000, i, "{\"cert\":\"C7e7Cn-7CnexPnexPn35\",\"pkg\":\"pj.ishuaji\",\"ua\":\"HTC+One+X\",\"mac\":\"353043055855897\",\"type\":2,\"cid\":\"tcp-server-test\",\"version\":\"15\"}");

            UserMsg.User user = UserMsg.User.newBuilder().setId(i).setAge(24).setName("穆书伟").setState(0).build();
            channel.write(user);
            Thread.sleep(1000);
            if (i == 100) {
                channel.close();
                System.exit(0);
            }
        }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

        SocketClient client = new SocketClient();
        client.init();
        client.run();
    }
}
