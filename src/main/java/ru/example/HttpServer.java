package ru.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.commons.cli.CommandLine;
import ru.example.netty.handler.HttpProcessingHandler;
import ru.example.server.config.CLIParser;
import ru.example.server.config.PropertiesLoader;
import ru.example.server.election.ElectionTimer;
import ru.example.server.heartbeat.HeartbeatTimer;
import ru.example.server.timer.ServerTimer;

import java.nio.charset.StandardCharsets;

/**
 * @author TaylakovSA
 */
public class HttpServer {

    public static void main(String[] args) throws Exception {
        CommandLine commandLine = initConfig(args);

        ServerTimer heartbeatTimer = new HeartbeatTimer();
        ServerTimer electionTimer = ElectionTimer.getInstance();
        HttpServer server = new HttpServer();

        String port = commandLine.getOptionValue("port");

        server.start(Integer.parseInt(port));

    }

    private static CommandLine initConfig(String[] args) {
        PropertiesLoader propertiesLoader = PropertiesLoader.getInstance();
        CLIParser cliParser = new CLIParser();
        CommandLine commandLine = cliParser.parseCLI(args);
        propertiesLoader.setProperty("election-timeout", commandLine.getOptionValue("election-timeout"));
        propertiesLoader.setProperty("heartbeat-timeout", commandLine.getOptionValue("heartbeat-timeout"));
        propertiesLoader.setProperty("node-id", commandLine.getOptionValue("node-id"));
        return commandLine;

    }

    public void start(int port) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap server = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(
                                            new HttpResponseEncoder(),
                                            new HttpRequestDecoder(),
                                            new HttpObjectAggregator(Integer.MAX_VALUE),
                                            new HttpProcessingHandler()
                                    );
                            ;
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 500)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            channelFuture = server.bind("localhost", port).sync();

            channelFuture.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            if (channelFuture != null) channelFuture.channel().close().awaitUninterruptibly();
        }

    }

    public static void sendError(ChannelHandlerContext ctx, String errorMessage, HttpResponseStatus status) {
        ByteBuf content = Unpooled.copiedBuffer(errorMessage, StandardCharsets.UTF_8);
        FullHttpResponse response =
                new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        status,
                        content);

        response.headers().set(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(HttpHeaders.Names.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());

        ChannelFuture channelFuture = ctx.writeAndFlush(response);
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }
}
