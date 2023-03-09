package cn.weifeng.chartroom.server;

import cn.weifeng.chartroom.handler.LoginRequestMassageHandler;
import cn.weifeng.chartroom.protocol.ChatProtocolFrameDecoder;
import cn.weifeng.chartroom.protocol.MessageCodecSharable;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup(6);
        ChatProtocolFrameDecoder PROTOCOL_DECODER = new ChatProtocolFrameDecoder();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        LoginRequestMassageHandler LOGIN_REQUEST = new LoginRequestMassageHandler();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 添加粘包半包处理器
                            ch.pipeline().addLast(PROTOCOL_DECODER);
                            // 添加打印日志
                            ch.pipeline().addLast(LOGGING_HANDLER);
                            // 添加消息处理器
                            ch.pipeline().addLast(MESSAGE_CODEC);
                            // 添加登录处理器
                            ch.pipeline().addLast(LOGIN_REQUEST);
                        }
                    });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("Netty Server Error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
