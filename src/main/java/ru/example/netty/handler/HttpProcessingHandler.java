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
import ru.example.netty.dto.RequestVoteDto;
import ru.example.server.election.ElectionService;
import ru.example.server.heartbeat.HeartbeatService;
import ru.example.utils.KryoConverter;

import java.nio.charset.StandardCharsets;

import static ru.example.netty.client.ApiUri.ELECTION;
import static ru.example.netty.client.ApiUri.HEARTBEAT;

/**
 * @author TaylakovSA
 */
public class HttpProcessingHandler extends ChannelInboundHandlerAdapter {

    private HeartbeatService heartbeatService;
    private ElectionService electionService;
    private KryoConverter converter;

    public HttpProcessingHandler() {
        this.heartbeatService = new HeartbeatService();
        this.converter = new KryoConverter();
        this.electionService = new ElectionService();
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
        if (HEARTBEAT.equals(request.getUri())) {
            HeartbeatRequestDto dto = (HeartbeatRequestDto) object;
            return heartbeatService.handle(dto);
        } else if (ELECTION.equals(request.getUri())) {
            RequestVoteDto dto = (RequestVoteDto) object;
            return electionService.vote(dto);
        }
        return null;
    }

}