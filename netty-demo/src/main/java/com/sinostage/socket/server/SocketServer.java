package com.sinostage.socket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author timkobe
 */
@Service("socketServer")
public class SocketServer {
    private Integer port=8088;

    private ChannelFuture channelFuture;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @PostConstruct
    public void init() {
        //创建boss线程组 用于服务端接受客户端的连接
        bossGroup = new NioEventLoopGroup();
        // 创建 worker 线程组 用于进行 SocketChannel 的数据读写
        workerGroup = new NioEventLoopGroup();
        // 创建 ServerBootstrap 对象
        ServerBootstrap b = new ServerBootstrap();
        //设置使用的EventLoopGroup
        b.group(bossGroup, workerGroup)
                //设置要被实例化的为 NioServerSocketChannel 类
                .channel(NioServerSocketChannel.class)
                // 设置 NioServerSocketChannel 的处理器
                .handler(new LoggingHandler(LogLevel.INFO))
                // 设置连入服务端的 Client 的 SocketChannel 的处理器
                .childHandler(new NettyServerInitializer());
        // 绑定端口，并同步等待成功，即启动服务端
        try {
            channelFuture = b.bind(port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @PreDestroy
    public void shutdown() throws InterruptedException {
        try {
            // 监听服务端关闭，并阻塞等待
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅关闭两个 EventLoopGroup 对象
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
