package ru.example.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ru.example.netty.decod.ResponseDataDecoder;
import ru.example.netty.encod.RequestDataEncoder;
import ru.example.netty.handler.ClientHandler;

import java.net.InetSocketAddress;

/**
 * @author TaylakovSA
 */
public class HttpClient {

    public static void main(String[] args) throws Exception {
        System.out.println("start");
        String host = "localhost";
        int port =8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.remoteAddress(new InetSocketAddress(host, port));
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(new RequestDataEncoder(),
                                    new ResponseDataDecoder(), new ClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect().sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
