package com.gtdq.netty.httpserver.core;


import com.gtdq.netty.utils.LogUtil;
import com.gtdq.netty.utils.ObjUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author : LiuMingyao
 * @date : 2019/8/31 23:17
 * @description : TODO
 */
public class RequestDispatcher {

    /**
     * @author : LiuMing
     * @date : 2019/8/31 23:21
     * @description :   分发不同类型的请求
     */
    public void dispatcher(ChannelHandlerContext ctx, HttpRequest request) {
        Map<String, Map<Method, Object>> uriMap = ObjUtil.getSingletonObj(UriMappingAnalysis.class).getUriMap();

        String uri = request.uri();
        LogUtil.infoLog("发起{}请求  请求的uri:{}", request.method().name(), uri);
        FullHttpResponse response;
        if (request.method().name().equals("GET")) {
            response = Request.get(uri, uriMap);
        } else {
            response = Request.get(uri, uriMap);
        }

        ctx.writeAndFlush(response);
    }


}