package com.gtdq.netty.httpserver.server;

import com.gtdq.netty.httpserver.core.RequestDispatcher;
import com.gtdq.netty.utils.ExceptionUtil;
import com.gtdq.netty.utils.LogUtil;
import com.gtdq.netty.utils.ObjUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : LiuMingyao
 * @date : 2019/8/31 14:14
 * @description : TODO
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final ExecutorService service = Executors.newFixedThreadPool(3);

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
        //当channel的上下文ctx移除channel的时候触发
        LogUtil.warnLog("{}断开", ctx.channel());

        /**
         * 如果使用了group保存了每一个add的channel，那么在断开的时候，netty会自动使用remove移除group中连接，不需要手动调用
         * ChannelGroup group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
         * group.remove(ctx.channel());
         */
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //当channel处于非活动状态，达到什么终结的时候被触发
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