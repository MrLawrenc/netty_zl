package com.gtdq.netty.httpserver.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author : LiuMingyao
 * @date : 2019/8/31 23:16
 * @description : TODO
 */
public class Response {

    public static FullHttpResponse getOkResponse(String responseMsg) {
        ByteBuf content = Unpooled.copiedBuffer(responseMsg, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        HttpHeaders headers = response.headers();
        headers.set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");//文本内容相应
        return response;
    }
}