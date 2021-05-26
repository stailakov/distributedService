package ru.example.netty.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpResponseStatus;
import ru.example.netty.dto.HeartbeatRequestDto;
import ru.example.netty.dto.HeartbeatResponseDto;
import ru.example.netty.model.RequestData;
import ru.example.netty.model.ResponseData;

/**
 * @author TaylakovSA
 */
@ChannelHandler.Sharable
public class HeartbeatProcessingHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        HeartbeatResponseDto responseData = new HeartbeatResponseDto();
        responseData.setId(1);
        responseData.setStatusCode("OK");
        ChannelFuture future = ctx.writeAndFlush(responseData);
        future.addListener(ChannelFutureListener.CLOSE);
        System.out.println(responseData);
    }
}