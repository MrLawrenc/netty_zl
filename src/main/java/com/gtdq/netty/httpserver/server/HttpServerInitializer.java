package com.gtdq.netty.httpserver.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author : LiuMingyao
 * @date : 2019/8/31 14:10
 * @description : TODO
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("netty",new HttpServerCodec());
        pipeline.addLast("myHandler",new HttpServerHandler());
    }
}