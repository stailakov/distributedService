package ru.example.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import ru.example.HttpServer;
import ru.example.netty.dto.HeartbeatResponseDto;
import ru.example.server.JsonUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TaylakovSA
 */
public class HttpProcessingHandler extends ChannelInboundHandlerAdapter {

    private Map<String, String> handlers;

    public HttpProcessingHandler() {
        handlers = new HashMap<>();
        handlers.put("/sum", "SUM");
        handlers.put("/test", "TEST");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        HttpServer.sendError(ctx, "ошибка сервера:" + cause.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        DefaultFullHttpRequest request = (DefaultFullHttpRequest) obj;
        System.out.println(request);
        HeartbeatResponseDto dto = new HeartbeatResponseDto();
        dto.setId(12);
        ByteBuf content = Unpooled.copiedBuffer(JsonUtil.toJsonString(dto), StandardCharsets.UTF_8);
        DefaultFullHttpResponse response =
                new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        content);

        response.headers().set(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(HttpHeaders.Names.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());

        ChannelFuture channelFuture = ctx.writeAndFlush(response);
        channelFuture.addListener(ChannelFutureListener.CLOSE);

        request.release();
    }
}