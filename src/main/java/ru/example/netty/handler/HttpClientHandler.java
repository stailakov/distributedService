package ru.example.netty.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import ru.example.server.JsonUtil;


import static io.netty.buffer.Unpooled.wrappedBuffer;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


/**
 * @author TaylakovSA
 */
public class HttpClientHandler<REQ, RES> extends ChannelInboundHandlerAdapter {

     private RES future = null;
     private REQ req = null;
     private Class<RES> responseClass = null;
     private final String uri;

     public HttpClientHandler(REQ req, Class<RES> responseClass, String uri){
         this.req = req;
         this.responseClass = responseClass;
         this.uri = uri;
     }

    public RES getFuture() {
        return future;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {

        FullHttpRequest request = new DefaultFullHttpRequest(HTTP_1_1, POST, uri,
                wrappedBuffer(JsonUtil.toJsonString(req).getBytes(CharsetUtil.UTF_8)));
//        request.headers().add(HttpHeaderNames.HOST, "127.0.0.1");
//        request.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
//        request.headers().add(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
//        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        ChannelFuture future = ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        FullHttpResponse response = (DefaultFullHttpResponse)msg;
        future = JsonUtil.toObject(response.content().array(), responseClass);
        ctx.close();

    }
}