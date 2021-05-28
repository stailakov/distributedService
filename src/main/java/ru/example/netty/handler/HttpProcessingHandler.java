package ru.example.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import ru.example.HttpServer;
import ru.example.netty.dto.HeartbeatRequestDto;
import ru.example.netty.dto.HeartbeatResponseDto;
import ru.example.server.KryoConverter;
import ru.example.server.handler.HeartbeatRequestHandler;

import java.nio.charset.StandardCharsets;

/**
 * @author TaylakovSA
 */
public class HttpProcessingHandler extends ChannelInboundHandlerAdapter {

    private HeartbeatRequestHandler heartbeatRequestHandler;
    private KryoConverter converter;

    public HttpProcessingHandler() {
        this.heartbeatRequestHandler = new HeartbeatRequestHandler();
        this.converter = new KryoConverter();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        HttpServer.sendError(ctx, "ошибка сервера:" + cause.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        FullHttpRequest request = (DefaultFullHttpRequest) obj;

        Object responseObj = handle(request);
        ByteBuf contentRes = converter.toByteBuf(responseObj);
        DefaultFullHttpResponse response =
                new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        contentRes);

        response.headers().set(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(HttpHeaders.Names.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());

        ChannelFuture channelFuture = ctx.writeAndFlush(response);
        channelFuture.addListener(ChannelFutureListener.CLOSE);

        request.release();
    }

    private Object handle(FullHttpRequest request) {
        ByteBuf content = request.content();
        Object object = converter.toObject(content);
        if ("/heartbeat".equals(request.getUri())) {
            HeartbeatRequestDto dto = (HeartbeatRequestDto) object;
            return heartbeatRequestHandler.handle(dto);
        }
        return null;
    }

}