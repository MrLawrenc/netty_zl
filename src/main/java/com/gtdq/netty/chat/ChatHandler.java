package com.gtdq.netty.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author : LiuMingyao
 * @date : 2019/9/1 16:10
 * @description : TODO
 */
public class ChatHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
        group.forEach(channel -> {
            if (channel == ctx.channel()) {
                channel.writeAndFlush("【自己】" + msg + "\n\t");
            } else {
                channel.writeAndFlush(channel.remoteAddress() + "说;" + msg + "\n\t");
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "将异常下线了");
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //会向group里面的每一个channel发送消息
        group.writeAndFlush("【服务端】" + ctx.channel().remoteAddress() + "  上线了!\n\t");
        group.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //netty会自动的从group中移除下线的channel,所以不是必须调用group.remove(ctx.channel())
        group.writeAndFlush("【服务端】" + ctx.channel().remoteAddress() + "  下线了!还剩" + group.size() + "个客户端在线!\n\t");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }


}