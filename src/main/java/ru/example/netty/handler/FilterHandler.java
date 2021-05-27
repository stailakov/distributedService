package ru.example.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.*;
import ru.example.HttpServer;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 * @author TaylakovSA
 */
public class FilterHandler extends MessageToMessageDecoder<DefaultFullHttpRequest> {

    private final Map<String, Set<HttpMethod>> requestMapping;

    public FilterHandler() {
        requestMapping = new HashMap<>();
        requestMapping.put("/heartbeat", Set.of(HttpMethod.POST));
        requestMapping.put("/election", Set.of(HttpMethod.POST));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultFullHttpRequest request, List<Object> out) throws Exception {

        String url = request.getUri();
        url = url == null ? "" : url.toLowerCase();
        if (!requestMapping.containsKey(url)) {
            HttpServer.sendError(ctx, "resource not found", HttpResponseStatus.NOT_FOUND);
            return;
        }

        Set<HttpMethod> methods = requestMapping.get(url);

        if (!methods.contains(request.getMethod())) {
            HttpServer.sendError(ctx, "method resource bot acceptable", HttpResponseStatus.NOT_ACCEPTABLE);
            return;
        }
        out.add(request);
        request.retain();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        HttpServer.sendError(ctx, "server error:" + cause.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }
}
