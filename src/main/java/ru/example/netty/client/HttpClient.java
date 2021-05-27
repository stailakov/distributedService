package ru.example.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.util.concurrent.FutureListener;

import ru.example.netty.handler.HttpClientHandler;

import java.net.InetSocketAddress;

/**
 * @author TaylakovSA
 */
public class HttpClient<REQ, RES> {

    public RES send(REQ req, Class<RES> resClass, String uri) throws InterruptedException {
        String host = "localhost";
        int port = 8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        HttpClientHandler<REQ, RES> httpClientHandler = new HttpClientHandler<>(req, resClass, uri);
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.remoteAddress(new InetSocketAddress(host, port));
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(
                                    new HttpRequestEncoder(),
                                    new HttpResponseDecoder(),
                                    new HttpObjectAggregator(Integer.MAX_VALUE),
                                    httpClientHandler

                            );
                }
            });

            ChannelFuture f = b.connect();
            f.addListener((FutureListener<Void>) future -> {
                if (!f.isSuccess()) {
                    throw new Exception("Test Connection failed");
                }
            });
            f.sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

        return httpClientHandler.getFuture();
    }

}
