package ru.example.netty.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.example.netty.dto.HeartbeatRequestDto;
import ru.example.netty.dto.HeartbeatResponseDto;
import ru.example.netty.model.RequestData;
import ru.example.netty.model.ResponseData;
import ru.example.server.JsonUtil;


/**
 * @author TaylakovSA
 */
@ChannelHandler.Sharable
public class HeartbeatClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {

        HeartbeatRequestDto requestDto = new HeartbeatRequestDto();
        requestDto.setLeaderId(1);
        ChannelFuture future = ctx.writeAndFlush(requestDto);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println((HeartbeatResponseDto)msg);
        ctx.close();
    }
}