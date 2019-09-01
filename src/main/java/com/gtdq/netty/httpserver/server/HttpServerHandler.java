package com.gtdq.netty.httpserver.server;

import com.gtdq.netty.httpserver.core.RequestDispatcher;
import com.gtdq.netty.utils.ExceptionUtil;
import com.gtdq.netty.utils.LogUtil;
import com.gtdq.netty.utils.ObjUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author : LiuMingyao
 * @date : 2019/8/31 14:14
 * @description : TODO
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            if (!request.uri().contains("/favicon.ico")) {
                ObjUtil.getSingletonObj(RequestDispatcher.class).dispatcher(ctx, request);
            }
        } else {
            LogUtil.infoLog("本次请求不是HTTP请求.........................");
        }
        ctx.channel().close();//无状态，每次都关闭，长连接使用websocket
    }

    public FullHttpResponse setHttpResp(String msg) {
        ByteBuf content = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        HttpHeaders headers = response.headers();
        headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain");//文本内容相应
        return response;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        LogUtil.infoLog("channelRegistered.....................");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LogUtil.infoLog("channelActive");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        LogUtil.warnLog("{}断开", ctx.channel());

        /**
         * 如果使用了group保存了每一个add的channel，那么在断开的时候，netty会自动使用remove移除group中连接，不需要手动调用
         * ChannelGroup group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
         * group.remove(ctx.channel());
         */
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LogUtil.infoLog("handlerAdded---与{}的连接建立", ctx.channel().remoteAddress());
        super.handlerAdded(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogUtil.infoLog("exceptionCaught{}", ExceptionUtil.appendExceptionInfo(cause));
        ctx.close();
    }
}