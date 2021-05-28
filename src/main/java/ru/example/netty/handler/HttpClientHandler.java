package ru.example.netty.handler;

import com.esotericsoftware.kryo.Kryo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import ru.example.server.KryoConverter;


import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


/**
 * @author TaylakovSA
 */
public class HttpClientHandler<REQ, RES> extends ChannelInboundHandlerAdapter {
    Kryo kryo = new Kryo();
     private RES future = null;
     private REQ req = null;
     private Class<RES> responseClass = null;
     private final String uri;
     private KryoConverter converter;

     public HttpClientHandler(REQ req, Class<RES> responseClass, String uri){
         this.req = req;
         this.responseClass = responseClass;
         this.uri = uri;
         this.converter = new KryoConverter();
     }

    public RES getFuture() {
        return future;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {

        ByteBuf content = converter.toByteBuf(req);

        FullHttpRequest request = new DefaultFullHttpRequest(HTTP_1_1, POST, uri, content);
        request.headers().set("Content-Length", content.readableBytes());
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
        ChannelFuture future = ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        FullHttpResponse response = (DefaultFullHttpResponse)msg;
        ByteBuf content = response.content();
        if (response.getStatus().equals(HttpResponseStatus.OK)){

            future = (RES)converter.toObject(content);
        } else {
            throw new Exception();
        }
        ctx.close();
    }
}